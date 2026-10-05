package com.GpgEncryptionDecryption

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openpgp.{PGPPublicKeyRingCollection, PGPUtil}
import org.bouncycastle.openpgp.jcajce.JcaPGPPublicKeyRingCollection

import java.io.ByteArrayInputStream
import java.security.Security
import java.util.Collections
import scala.collection.JavaConverters._
import scala.io.Source

object ReadPubKeys {
  def main(args: Array[String]): Unit = {

    Security.addProvider(new BouncyCastleProvider())

    // (A) The two exported public keys, read as Strings — this mirrors `sdkPubKeys: Seq[String]`
    val keyPaths = Seq(
        "/Users/dharanikumar/gpg-lab/alice_pub.asc",
        "/Users/dharanikumar/gpg-lab/bob_pub.asc"
    )
    val armoredKeys: Seq[String] = keyPaths.map(p => Source.fromFile(p).mkString)

    // (B) The readPubKeys-equivalent: parse each armored block, merge all rings into ONE collection
    val collection: PGPPublicKeyRingCollection = readPubKeys(armoredKeys)

    // (C) Visualize: one ring per identity; each ring has a master key + (usually) an encryption subkey
    println(s"Total key rings in collection: ${collection.size()}\n")
    for (ring <- collection.getKeyRings.asScala) {
      val uids = ring.getPublicKey.getUserIDs.asScala.toList.mkString(", ")
      println(s"=== Key ring: $uids ===")
      for (key <- ring.getPublicKeys.asScala) {
        val keyId = f"${key.getKeyID}%016X" // the Key ID verification looks up by
        println(f"  keyId=$keyId  master=${key.isMasterKey}%-5s  canEncrypt=${key.isEncryptionKey}%-5s  algoId=${key.getAlgorithm}")
      }
      println()
    }

    // (D) Prove the lookup-by-KeyID that signature verification uses (getPublicKey(onePassSignature.getKeyID))
    val someKeyId = collection.getKeyRings.asScala.next().getPublicKey.getKeyID
    val found = Option(collection.getPublicKey(someKeyId))
    println(s"Lookup keyId=${f"$someKeyId%016X"} -> ${if (found.isDefined) "FOUND ✅" else "not found ❌"}")
  }

  /** Condensed equivalent of Serin's CryptoUtils.readPubKeys:
   * take many armored public keys, parse each, and MERGE every ring into one collection. */
  def readPubKeys(armoredKeys: Seq[String]): PGPPublicKeyRingCollection = {
    val perKeyCollections: Seq[JcaPGPPublicKeyRingCollection] =
      armoredKeys.distinct.map { armored =>
        val in = PGPUtil.getDecoderStream(new ByteArrayInputStream(armored.getBytes))
        new JcaPGPPublicKeyRingCollection(in) // same class Serin uses
      }

    // fold every ring from every parsed key into one combined collection (Serin's merge step)
    perKeyCollections.foldLeft(new PGPPublicKeyRingCollection(Collections.emptyList())) { (acc, coll) =>
      coll.getKeyRings.asScala.foldLeft(acc) { (a, ring) =>
        PGPPublicKeyRingCollection.addPublicKeyRing(a, ring) // same static merge call Serin uses
      }
    }
  }
}
