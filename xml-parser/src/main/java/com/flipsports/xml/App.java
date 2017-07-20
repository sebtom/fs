package com.flipsports.xml;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import com.flipsports.xml.XmlReader.XmlFilename;

/* depending on the integration solution - ideally this would not be executed by demand
 * the system could monitor if new files arrives, or gets feed from some API like kafka
 * so the APP class is totally tmp solution to be replaced to fit integration solutions
 * */
public class App {
    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("parser");
        try {
            ActorRef reader = system.actorOf(XmlReader.props(), "reader");
            Inbox inbox = Inbox.create(system);

//            String dir = "/tmp/wiki/"
//            val file = if (false) "enwiki_part1.xml" // shorter partial file
//            else "enwikivoyage-20140520-pages-articles.xml" // full unpacked XML dump of Wiki-Voyage

            inbox.send(reader, new XmlFilename("/home/stomaszewski/_PRIV/fs/xml-parser/src/main/resources/xml/f40.xml"));

        } finally {
            system.terminate();
        }

//
//
//        implicit val system = ActorSystem("parser")
//        //  val log = Logger
//
//        val reader = system.actorOf(Props[XmlReader].withRouter(RoundRobinRouter(2)), "reader")
//        val parser = system.actorOf(Props[ArticleParser].withRouter(RoundRobinRouter(2)), "article")
//        val art = system.actorOf(Props[LongestArticle].withRouter(RoundRobinRouter(2)), "longestArticle")
//        val geo = system.actorOf(Props[ArticleGeoParser].withRouter(RoundRobinRouter(1)), "geoParser")
//        val seePl = system.actorOf(Props[ArticleSeePlacesParser].withRouter(RoundRobinRouter(2)), "seePlaces")
//
//        val met = system.actorOf(Props[Metrics].withRouter(RoundRobinRouter(2)), "metrics")
//
//        val agentCount = Agent(0)
//        val agentMaxArtTitle = Agent("")
//        val agentMaxArtTitleLen = Agent(0)
//
//        println("Sending flnm")
//        val inbox = Inbox.create(system)
//
//        val dir = "/tmp/wiki/"
//        val file = if (false) "enwiki_part1.xml" // shorter partial file
//        else "enwikivoyage-20140520-pages-articles.xml" // full unpacked XML dump of Wiki-Voyage
//
//        inbox.send(reader, XmlFilename(dir + file))

        // further approach to optimise:
//  for (file <- List("enwiki_140_p1.xml", "enwiki_140_p2.xml")) // two chunks of whole XML
//    inbox.send(reader, XmlFilename(dir + file))

    }
}









