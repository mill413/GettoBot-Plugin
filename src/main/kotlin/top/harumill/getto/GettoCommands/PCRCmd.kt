package top.harumill.getto.GettoCommands

import net.mamoe.mirai.contact.Contact
import top.harumill.getto.Getto
import top.harumill.getto.GettoCommands.GettoCommand
import top.harumill.getto.UserLevel
import java.io.File

class PCRCmd(_args: List<String>, _level: UserLevel) : GettoCommand(_args, _level) {
    val comicDir = "${Getto.imgDir}pcr/comic/"
    val stampDir = "${Getto.imgDir}pcr/stamp/"

    override fun execute(receiver: Contact, sender: Contact) {
        when(args[0]){
            "comic" -> {
                val fileTree = File(comicDir).walk()
                    .maxDepth(1)
                    .filter { it.isFile }
                    .forEach {  }
            }
            "stamp" -> {

            }
        }
    }
}