package src.main.java.storm.spouts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;



public class ParseLogSpout implements IRichSpout 
{
    private SpoutOutputCollector collector;
    private FileReader fileReader;
    private boolean completed = false;
    private TopologyContext context;
	
	
    @Override
    public void open(Map conf, TopologyContext context,
            SpoutOutputCollector collector) 
    {
        try {
            this.context = context;
			//Make sure that you mention the path of the file correctly.This is a very bad approach.
			//Try to put these details in a config file or give the user an option to pick up the file
			
			//Also, It's a good practice to TRY out file reading. Otherwise, the program ends abruptly.
            String filePath= "C:/PatientData.txt";
            this.fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error reading file "
                    + conf.get("inputFile"));
        }
        this.collector = collector;
    }
    
	//nextTuple method defines the work of spout. Here we parse the file line by line and emit all sentences tht were being read
    @Override
    public void nextTuple() 
    {
        if (completed) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
        String str;
        BufferedReader reader = new BufferedReader(fileReader);
        try {
            while ((str = reader.readLine()) != null) {
                this.collector.emit(new Values(str), str);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading tuple", e);
        } finally {
            completed = true;
        }
    }

	//Declaring the output field. This allows other components to know what kindof output is being expected from this spout
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) 
    {
        declarer.declare(new Fields("line"));
    }
	
	// Gracefully closing the filereader
    @Override
    public void close() {
        try {
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean isDistributed() {
        return false;
    }
    @Override
    public void activate() {
    }
    @Override
    public void deactivate() {
    }
    @Override
    public void ack(Object msgId) {
    }
    @Override
    public void fail(Object msgId) {
    }
    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}