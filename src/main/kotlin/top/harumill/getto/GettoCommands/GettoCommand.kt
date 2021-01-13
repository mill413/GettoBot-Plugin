package top.harumill.getto.GettoCommands

import top.harumill.getto.UserLevel

/**
 * 指令类
 * name : 指令名称
 * args : 指令后的参数
 * level : 指令的级别
 *         不同指令前缀表示不同的级别
 *         / - Administrator（bot所有者）
 *         ~ - Owner（群主or管理员）
 *         # - Normal（所有人）
 *         指令级别向下兼容
 */
abstract class GettoCommand {
    val name:String = ""
    val args:List<String> = mutableListOf()
    val level: UserLevel = UserLevel.Normal

    abstract fun parseCmd(msg: String)
    abstract fun execute()
}