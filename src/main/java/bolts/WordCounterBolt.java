package src.main.java.storm.bolts;

import src.main.java.storm.bolts.Histogram;

import java.util.HashMap;
import java.util.Map;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.SwingUtilities;

/*
This Bolt takes the input from SearchImageBolt and maintains a hashmap. If a word already exists, it increments the counter. Otherwise, it will insert the word and sets its value to ONE. Later, we sort the hashmap and show only top 5 entries with their values 
*/
public class WordCounterBolt implements IRichBolt 
{
	Map<String, Integer> counters,sortedMapAsc;
	private OutputCollector collector;
	public static boolean ASC = true;
	public static boolean DESC = false;

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) 
	{
		this.counters = new HashMap<String, Integer>();
		this.collector = collector;
	}

	//The logic looks for input containing only alphabets A-Z and a-z
	@Override
	public void execute(Tuple input) 
	{
		String str = input.getString(0);
		if(str.matches(".*[a-zA-Z]+.*"))
		{
		if(!counters.containsKey(str)){
			counters.put(str, 1);
		}else{
			Integer c = counters.get(str) +1;
			counters.put(str, c);
		}
		}
		collector.ack(input);
	}
	
	//Cleanup method has the code that displays the hasmap in bar graph. When a toplogy is killed, the cleanup code gets executed.
	@Override
	public void cleanup() 
	{	
		//Sorting the current map
		sortedMapAsc = sortByComparator(counters, DESC);
		
		for(Map.Entry<String, Integer> entry:sortedMapAsc.entrySet())
		{
			System.out.println(entry.getKey()+" : " + entry.getValue());
		}
		
		//Invoking the histogram class by sending the sorted map
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Histogram histogram = new Histogram(sortedMapAsc);
            }
        });	
	}
	
	@Override
	public void declareOutputFields( OutputFieldsDeclarer declarer ) 
    {
       
    }
	@Override
	public Map<String, Object> getComponentConfiguration() 
	{
		return null;
	}
	
	public static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order)
    {

        List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>()
        {
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
	
}
