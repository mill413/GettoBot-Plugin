package top.harumill.getto

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.MessageChain
import top.harumill.getto.GettoCommands.GettoCommand
import top.harumill.getto.GettoCommands.PCRCmd
import java.io.File
import java.net.URL
import java.util.zip.GZIPInputStream

object Getto {
    val id:Long = 845689905
    val pwd:String = "hawkin"
    val authorId:Long = 501848752
    val wifeID:Long = 1234567

    val imgDir = "data/img/"
    /**
     * 下载文件
     */
    fun downloadFile(url: String, fileName:String): File {
        val openConnection = URL(url).openConnection()
        val type = openConnection.contentType
        val file:File = File(fileName+type)
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

    suspend fun parseCmd(message:MessageChain, receiver:Contact,event:MessageEvent) {
        val cmdLevel:UserLevel
        val cmdArgs:List<String>
        val cmdName:String

        val msg = message.contentToString()
        //解析指令前缀
        when(msg[0]){
            '/' -> {
                cmdLevel = UserLevel.Owner
                msg.removePrefix("/")
            }
            '~' -> {
                cmdLevel = UserLevel.Operator
                msg.removePrefix("~")
            }
            '#' -> {
                cmdLevel = UserLevel.Normal
                msg.removePrefix("#")
            }
            else -> {
                cmdLevel = UserLevel.Normal
            }
        }
        //获取指令名称
        cmdArgs = msg.split(' ').toList()
        if(cmdArgs.isEmpty()){
            receiver.sendMessage("输入#help来获取bot可用指令哦")
            return
        }
        cmdName = cmdArgs[0]
        when(cmdName){
            "pcr" -> PCRCmd(cmdArgs,cmdLevel).execute(receiver,event.sender)


        }
    }


}