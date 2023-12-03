package persistence

/**
 * This interface provides a contract for serialization and deserialization.
 */
interface Serializer {

    /**
     * Function to write an object to a persistent storage.
     *
     * @param obj The object to write.
     * @throws Exception If an error occurs during writing.
     */
    @Throws(Exception::class)
    fun write(obj: Any?)

    /**
     * Function to read an object from a persistent storage.
     *
     * @throws Exception If an error occurs during reading.
     * @return The object read from the storage.
     */
    @Throws(Exception::class)
    fun read(): Any?
}
