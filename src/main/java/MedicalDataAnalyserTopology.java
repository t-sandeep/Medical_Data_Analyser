package src.main.java.storm;

import src.main.java.storm.spouts.ParseLogSpout;
import src.main.java.storm.bolts.SearchImageBolt;
import src.main.java.storm.bolts.WordCounterBolt;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class MedicalDataAnalyserTopology 
{
    public static void main(String[] args) throws Exception 
    {
		//Creating TopologyBuilder object
        TopologyBuilder builder = new TopologyBuilder();
		
		//Setting up the topology | Spout -> SearchImageBolt -> Wordcounter 
        builder.setSpout( "Spout", new ParseLogSpout() );
        builder.setBolt("SearchImageBolt", new SearchImageBolt()).shuffleGrouping("Spout");
        builder.setBolt("WordCounter", new WordCounterBolt()).shuffleGrouping("SearchImageBolt");

		// create the default config object
        Config conf = new Config();    
		
		// create the local cluster instance
        LocalCluster cluster = new LocalCluster();
		
		// create the topology and submit with config
        cluster.submitTopology("MedicalDataAnalyserTopology", conf, builder.createTopology());
		
		// let the topology run for 1.5 seconds. note topologies never terminate!
        Utils.sleep(15000);  
		
		// now kill the topology
		cluster.killTopology("MedicalDataAnalyserTopology");

		// we are done, so shutdown the local cluster
		cluster.shutdown();
    }
}