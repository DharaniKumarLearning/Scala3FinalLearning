package com.GpgEncryptionDecryption

import java.io._
import java.security.{SecureRandom, Security}
import java.util.Date

import org.bouncycastle.bcpg.{ArmoredOutputStream, CompressionAlgorithmTags, SymmetricKeyAlgorithmTags}
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openpgp._
import org.bouncycastle.openpgp.jcajce.{JcaPGPObjectFactory, JcaPGPPublicKeyRingCollection}
import org.bouncycastle.openpgp.operator.jcajce._


object PGPDecryption {

  Security.addProvider(new BouncyCastleProvider())
  private val BufferSize = 1 << 16

  private def findPrivateKey(secRings: PGPSecretKeyRingCollection, keyID: Long, pass: Array[Char]): PGPPrivateKey = {
    val secKey = secRings.getSecretKey(keyID)
    if (secKey == null) return null
    secKey.extractPrivateKey(new JcePBESecretKeyDecryptorBuilder().setProvider("BC").build(pass))
  }

  def decryptFile(inputPath: String, outputPath: String, secretKeyPath: String, passphrase: String): Unit = {
    val in = PGPUtil.getDecoderStream(new BufferedInputStream(new FileInputStream(inputPath)))
    val secRings = new PGPSecretKeyRingCollection(
      PGPUtil.getDecoderStream(new FileInputStream(secretKeyPath)), new JcaKeyFingerprintCalculator())

    val factory = new JcaPGPObjectFactory(in)
    val encList = factory.nextObject() match {
      case e: PGPEncryptedDataList => e
      case _ => factory.nextObject().asInstanceOf[PGPEncryptedDataList]
    }

    var privKey: PGPPrivateKey = null
    var pbe: PGPPublicKeyEncryptedData = null
    val objs = encList.getEncryptedDataObjects
    while (privKey == null && objs.hasNext) { // try each recipient block
      pbe = objs.next().asInstanceOf[PGPPublicKeyEncryptedData]
      privKey = findPrivateKey(secRings, pbe.getKeyID, passphrase.toCharArray)
    }
    if (privKey == null) throw new IllegalArgumentException("No matching secret key / wrong passphrase")

    val clear = pbe.getDataStream(new JcePublicKeyDataDecryptorFactoryBuilder().setProvider("BC").build(privKey))
    var plainFactory = new JcaPGPObjectFactory(clear)
    var msg = plainFactory.nextObject()
    msg match {
      case data: PGPCompressedData =>
        plainFactory = new JcaPGPObjectFactory(data.getDataStream)
        msg = plainFactory.nextObject()
      case _ =>
    }
    msg match {
      case ld: PGPLiteralData =>
        val unc = ld.getInputStream
        val fos = new BufferedOutputStream(new FileOutputStream(outputPath))
        try {
          val buf = new Array[Byte](BufferSize);
          var n = unc.read(buf)
          while (n > 0) {
            fos.write(buf, 0, n); n = unc.read(buf)
          }
        } finally fos.close()
      case other => throw new IllegalStateException("Unexpected packet: " + other.getClass)
    }

    if (pbe.isIntegrityProtected && !pbe.verify()) // tamper check (== gpg MDC)
      throw new PGPException("Integrity check FAILED — file tampered with")
    println(s"Decrypted -> $outputPath (integrity OK)")
  }

  def main(args: Array[String]): Unit = {
    val lab = "/Users/dharanikumar/gpg-lab"
    decryptFile(s"$lab/scala-encrypted.gpg", s"$lab/scala-decrypted.zip", s"$lab/dharani-secret.asc", "Amma@3539")
  }
}
