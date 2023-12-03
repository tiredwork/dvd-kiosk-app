package persistence

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver
import models.Customer
import models.Media
import java.io.File
import java.io.FileReader
import java.io.FileWriter

/**
 * This class provides a JSON serializer for persistence.
 *
 * @property file The file to read from and write to.
 */
class JSONSerializer(private val file: File) : Serializer {

    /**
     * Function to read an object from a file.
     *
     * @throws Exception If an error occurs during reading.
     * @return The object read from the file.
     */
    @Throws(Exception::class)
    override fun read(): Any {
        val xStream = XStream(JettisonMappedXmlDriver())
        xStream.allowTypes(arrayOf(Media::class.java))
        xStream.allowTypes(arrayOf(Customer::class.java))
        val inputStream = xStream.createObjectInputStream(FileReader(file))
        val obj = inputStream.readObject() as Any
        inputStream.close()
        return obj
    }

    /**
     * Function to write an object to a file.
     *
     * @param obj The object to write.
     * @throws Exception If an error occurs during writing.
     */
    @Throws(Exception::class)
    override fun write(obj: Any?) {
        val xStream = XStream(JettisonMappedXmlDriver())
        val outputStream = xStream.createObjectOutputStream(FileWriter(file))
        outputStream.writeObject(obj)
        outputStream.close()
    }
}
