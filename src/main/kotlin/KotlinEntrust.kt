/**
 * 委托
 *
 */
interface DB {
    fun save()
}

class SqlDB() : DB {
    override fun save() {
        println("save to sql")
    }
}

class GreenDaoDB() : DB {
    override fun save() {
        println("save to greenDaoDB")

    }

}

//db参数 通过 by 将接口实现委托给db
class UniversalDB(db: DB) : DB by db

/**
 * 委托属性(直接委托）
 */
class Item{
    var count:Int=0
    var total:Int by ::count
}

fun main() {
    UniversalDB(SqlDB()).save()
    UniversalDB(GreenDaoDB()).save()
}