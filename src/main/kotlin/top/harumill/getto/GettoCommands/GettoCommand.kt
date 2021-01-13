package top.harumill.getto.GettoCommands

import net.mamoe.mirai.contact.Contact
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
abstract class GettoCommand (_args:List<String>,_level:UserLevel){

    var name:String = ""
    var args:List<String> = mutableListOf()
    var level: UserLevel = UserLevel.Normal

    init {
        name = _args[0]
        args = _args.subList(1,_args.lastIndex)
        level = _level
    }
    abstract fun execute(receiver:Contact,sender:Contact)
}