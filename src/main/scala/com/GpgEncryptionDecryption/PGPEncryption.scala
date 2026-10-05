package com.GpgEncryptionDecryption

import java.io._
import java.security.{SecureRandom, Security}
import java.util.Date

import org.bouncycastle.bcpg.{ArmoredOutputStream, CompressionAlgorithmTags, SymmetricKeyAlgorithmTags}
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openpgp._
import org.bouncycastle.openpgp.jcajce.{JcaPGPObjectFactory, JcaPGPPublicKeyRingCollection}
import org.bouncycastle.openpgp.operator.jcajce._

object PGPEncryption {

  Security.addProvider(new BouncyCastleProvider())
  private val BufferSize = 1 << 16

  import org.bouncycastle.bcpg.sig.KeyFlags

  private def canEncrypt(k: PGPPublicKey): Boolean = {
    if (!k.isEncryptionKey) return false
    val sigs = k.getSignatures
    while (sigs.hasNext) {
      val hashed = sigs.next().getHashedSubPackets
      if (hashed != null && hashed.getKeyFlags != 0)
        return (hashed.getKeyFlags & (KeyFlags.ENCRYPT_COMMS | KeyFlags.ENCRYPT_STORAGE)) != 0
    }
    false
  }

  def readPublicKey(path: String): PGPPublicKey = {
    val in = PGPUtil.getDecoderStream(new FileInputStream(path))
    try {
      val rings = new JcaPGPPublicKeyRingCollection(in)
      val it = rings.getKeyRings
      while (it.hasNext) {
        val keys = it.next().getPublicKeys
        while (keys.hasNext) {
          val k = keys.next(); if (canEncrypt(k)) return k
        }
      }
      throw new IllegalArgumentException("No encryption key in " + path)
    } finally in.close()
  }

  // encrypting the file
  def encryptFile(inputPath: String, outputPath: String, publicKeyPath: String, armor: Boolean = false): Unit = {
    val encKey = readPublicKey(publicKeyPath)
    var out: OutputStream = new BufferedOutputStream(new FileOutputStream(outputPath))
    if (armor) out = new ArmoredOutputStream(out)

    val encGen = new PGPEncryptedDataGenerator(
      new JcePGPDataEncryptorBuilder(SymmetricKeyAlgorithmTags.AES_256)
        .setWithIntegrityPacket(true)
        .setSecureRandom(new SecureRandom())
        .setProvider("BC"))  // this is just a recipe like scramble the data with , add a tamper check -- no keys exist after this line
    encGen.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(encKey).setProvider("BC"))  // this line will just make sure who should be able to open the encrypted file
    val encOut = encGen.open(out, new Array[Byte](BufferSize))  // generates the random session key here

    val compGen = new PGPCompressedDataGenerator(CompressionAlgorithmTags.UNCOMPRESSED)
    val compOut = compGen.open(encOut)

    val litGen = new PGPLiteralDataGenerator()
    val file = new File(inputPath)
    val litOut = litGen.open(
      compOut, PGPLiteralData.BINARY, file.getName, file.length(), new Date(file.lastModified()))

    val fis = new FileInputStream(file)
    try {
      val buf = new Array[Byte](BufferSize)
      var n = fis.read(buf)
      while (n > 0) {
        litOut.write(buf, 0, n)
        n = fis.read(buf)
      }
    } finally fis.close()

    litGen.close()
    compGen.close()
    encGen.close()
    out.close()

  }

  def main(args: Array[String]): Unit = {
    val lab = "/Users/dharanikumar/gpg-lab"
    encryptFile(s"$lab/secret-bundle.zip", s"$lab/scala-encrypted.gpg", s"$lab/dharani-public.asc")
  }

}
