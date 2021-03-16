package top.harumill.getto

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

object GettoDB {
    private const val driver = "com.mysql.cj.jdbc.Driver"
    private const val url =
        "jdbc:mysql://localhost:3306/bot?useSSL=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&autoReconnect=true"
    private const val user = "root"
    private const val psd = "rootroot"

    private var conn:Connection
    var stat:Statement
    init {
        Class.forName(driver)
        conn = DriverManager.getConnection(url, user, psd)
        stat = conn.createStatement()
    }

    private fun connect(){
        conn = DriverManager.getConnection(url, user, psd)
        stat = conn.createStatement()
    }

    fun query(op: String): ResultSet {
//        if (conn.isClosed or stat.isClosed) connect()
        stat = conn.createStatement()
        return stat.executeQuery(op)
    }

    fun update(op: String){
//        if (conn.isClosed or stat.isClosed) connect()
        stat = conn.createStatement()
        stat.executeUpdate(op)
    }
}