package src.main.java.storm.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*

*/
public class SearchImageBolt extends BaseRichBolt 
{
    private OutputCollector collector;

    public void prepare( Map conf, TopologyContext context, OutputCollector collector ) 
    {
        this.collector = collector;
    }
	
	//execute method is the heart of SearchImageBolt. Here we break/split the sentence coming from ParseLogSpout into words and take the field we are interested in. Then emit that word(s)
    public void execute( Tuple tuple ) 
    {
        String sentence = tuple.getString(0);
		//Splitting the sentence based on comma
        String[] words = sentence.split(",");
		
		int counter = 0;
        for(String word: words){
            counter++;
			word = word.trim();
			//I am checking for the 9th word. This was my requirement. You can change this as per your requirement
            if(!word.isEmpty() && counter==9){
                collector.emit(new Values(word));
            }
        }
        collector.ack( tuple );
    }
	//Declaring the output being expected from this bolt
    public void declareOutputFields( OutputFieldsDeclarer declarer ) 
    {
        declarer.declare(new Fields("word"));
    }   
}