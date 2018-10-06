
object DuwamishStream {

  def main(args: Array[String]): Unit = {

    import com.mongodb.BasicDBList
    import com.mongodb.BasicDBObject

    val list = new BasicDBList

    list.add(new BasicDBObject(
      "maxCitationCount", 1
    ))

    list.add(new BasicDBObject(
      "maxCitationCount", 2
    ))

    import scala.compat.java8.StreamConverters._

    final case class MaxMin(max: Int)

    def toList(dbObj: BasicDBList): Stream[MaxMin] = {
      dbObj.parallelStream().toScala[Stream].map {
        case (o: BasicDBObject) =>
          println(o)
          MaxMin(o.getInt("maxCitationCount"))
      }
    }

    //evaluate stream
    val result = toList(list)
    println(result) // List(MaxMin(1), MaxMin(2))

  }
}
