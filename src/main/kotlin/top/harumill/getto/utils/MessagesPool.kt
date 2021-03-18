package top.harumill.getto.utils

import net.mamoe.mirai.message.data.*
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

object MessagesPool {
    private val msg_lst:LinkedList<MyMessage> = LinkedList()

    fun insert(msg: MessageChain,time:LocalDateTime) {
        val myMsg = MyMessage(msg,time)
        msg_lst.offer(myMsg)
        while(msg_lst.isNotEmpty()){
            val topMsgTime = msg_lst.peek().time
            val duration = Duration.between(topMsgTime,time)
            val dif = duration.toMillis()
//            println(duration.toMillis())
            if(dif > 2*60*60*1000){
//                println("消息已过期{[id:${msg_lst.peek().msg.ids[0]}],[time:${topMsgTime}]}")
                msg_lst.poll()
            }
            else{
                break
            }
        }
//        println("insert后存储的消息数量:${msg_lst.size}")
    }

    fun getById(id: Int): MessageChain?{
//        println("get后存储消息数量${msg_lst.size}")
        msg_lst.forEach {
            if(it.msg.ids[0] == id){
                return it.msg
            }
        }
        return null
    }
}