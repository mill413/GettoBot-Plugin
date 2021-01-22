package top.harumill.getto.bot

import java.io.File
import java.net.URL
import java.util.zip.GZIPInputStream

object Getto {

    /**
     * 下载文件
     */
    fun downloadFile(url: String, fileName:String): File {
        val openConnection = URL(url).openConnection()
        val type = openConnection.contentType
        val file = File(fileName+type)
        //防止某些网站跳转到验证界面
        openConnection.addRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
        //如果图片是采用gzip压缩
        val bytes = if (openConnection.contentEncoding == "gzip") {
            GZIPInputStream(openConnection.getInputStream()).readBytes()
        } else {
            openConnection.getInputStream().readBytes()
        }
        file.writeBytes(bytes)
        return file
    }

    fun getImgList(path: String): MutableList<String> {
        val files:MutableList<String> = mutableListOf()
        val fileTree:FileTreeWalk = File(path).walk()
        fileTree.maxDepth(1)
            .filter { it.isFile }
            .forEach { files.add(it.name) }
        return files
    }

}