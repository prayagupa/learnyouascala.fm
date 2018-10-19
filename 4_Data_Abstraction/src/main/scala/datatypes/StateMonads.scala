package datatypes

import java.time.OffsetDateTime

///
/// https://typelevel.org/cats/datatypes/freemonad.html
///

object StateMonads {

  def main(args: Array[String]): Unit = {

    import cats.data.State
    val configState = State[Map[String, Map[String, String]], String] { state =>
      (state, s"The state is updated at ${OffsetDateTime.now()}")
    }

    val newConfState = configState.run(readLocalState)
      .map { case (st, log) =>

        def fetchState = readRemoteState

        val newState = fetchState.map { ns =>
          st + (ns._1 -> ns._2)
        }

        (newState, s"State updated from remote at ${OffsetDateTime.now()}")
      }

    println(newConfState.value)
  }

  def readLocalState = Map(
    "account-related" -> Map(
      "intent" -> "account-related",
      "phone" -> "999-999-9999"
    )
  )

  def readRemoteState = Map(
    "account-related" -> Map(
      "intent" -> "account",
      "phone" -> "999-999-8888"
    ),
    "credit-related" -> Map(
      "intent" -> "credit",
      "phone" -> "888-888-8888"
    )
  )

}
