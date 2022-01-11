package gui

import java.util.*
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.*

class BloomFilter(var sizeBloomFilter: Int, var numExpectedElements: Int = 100000) {

    var bloomFilter = BitSet()
    var numHashFunctions = 0
    var hash = "MD5"

    init {
        bloomFilter = BitSet(sizeBloomFilter)
        bloomFilter.clear()
        // Compute optimal number of hash functions.
        numHashFunctions = ((sizeBloomFilter.toDouble() / numExpectedElements).roundToInt() * log10(2.0)).toInt()
    }

    // Hashes the passed string with the passed hashing algorithm.
    private fun hash(input: String, hash: String): Int {
        val md = MessageDigest.getInstance(hash)
        var x = BigInteger(1, md.digest(input.toByteArray())).toInt() / sizeBloomFilter
        return if (x > 0) {
            x
        } else {
            -x
        }
    }

    // Appends a number for each input in the hash function.
    // Since any difference in the input of a hash function results
    // in a different hash, the result can be seen as a different hash function.
    // This is a trick to have K hash functions instead of having different hash functions.
    private fun hash(input: String, k: Int, hash: String): Int {
        return hash(k.toString() + input, hash)
    }

    // Adds item to Bloom Filter,
    // by iterating through all the hash functions.
    fun add(input: String) {
        for (i in 0..numHashFunctions) {
            println("Size: " + bloomFilter.size())
            var x = hash(input, i, hash)
            println("X: $x")
            bloomFilter.set(x)
        }
    }

    // Checks if an item does not belong to a BloomFilter.
    // Returns true if it doesn't belong.
    fun checkIfNotInFilter(input: String): Boolean {
        for (i in 0..numHashFunctions) {
            if (!bloomFilter[hash(input, i, hash)]) {
                return true
            }
        }
        return false
    }

   
}