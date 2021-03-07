package top.harumill.getto

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

object GettoDB {
    val driver = "com.mysql.cj.jdbc.Driver"
    val url =
        "jdbc:mysql://localhost:3306/bot?useSSL=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8"
    val user = "root"
    val psd = "rootroot"

    var conn:Connection
    var stat:Statement

    init {
        Class.forName(driver)
        conn = DriverManager.getConnection(url, user, psd)
        stat = conn.createStatement()
    }

    fun query(op:String):ResultSet{
        return stat.executeQuery(op)
    }

    fun insert(op: String){
        stat.executeUpdate(op)
    }
}