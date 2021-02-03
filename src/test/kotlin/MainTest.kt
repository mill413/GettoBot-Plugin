package top.harumill.getto

import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import top.harumill.getto.botInfo.GettoInfo
import java.io.File

@ConsoleExperimentalApi
suspend fun main() {

    MiraiConsoleTerminalLoader.startAsDaemon()

    PluginMain.load()
    PluginMain.enable()


    MiraiConsole.addBot(id, pwd) {
        fileBasedDeviceInfo()
    }.alsoLogin()

    MiraiConsole.job.join()
}