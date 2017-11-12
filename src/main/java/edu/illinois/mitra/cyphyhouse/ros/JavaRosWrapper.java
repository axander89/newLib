package edu.illinois.mitra.cyphyhouse.ros;

import java.util.*;
import java.io.*;

import com.fasterxml.jackson.databind.JsonNode;
import ros.Publisher;
import ros.RosBridge;
import ros.RosListenDelegate;
import ros.SubscriptionRequestMsg;
import ros.msgs.sensor_msgs.LaserScan;
import ros.msgs.geometry_msgs.Point;
import ros.msgs.std_msgs.PrimitiveMsg;
import ros.tools.MessageUnpacker;
import ros.RosBridge;


import edu.illinois.mitra.cyphyhouse.objects.ItemPosition;
import edu.illinois.mitra.cyphyhouse.gvh.GlobalVarHolder;
import edu.illinois.mitra.cyphyhouse.models.Model_iRobot;
import edu.illinois.mitra.cyphyhouse.models.Model_Car;

public class JavaRosWrapper {
	public RosBridge bridge;
	public String robot_name;
	public List<Publisher> publishers;
	public List<String> subscribed_topics;
	public int count;
	public GlobalVarHolder gvh;
	public Model_Car model_car;
	public Model_iRobot model_irobot;
	public String platform;

	public JavaRosWrapper(String port, String name, GlobalVarHolder gvh, String platform){
		this.robot_name = name;
		this.publishers = new ArrayList<Publisher>();
		this.subscribed_topics = new ArrayList<String>();
		this.gvh = gvh;

		switch(platform){
			case "Car":
				this.model_car = (Model_Car)gvh.gps.getMyPosition();
				break;
			case "iRobot":
				//this.model_irobot = (Model_iRobot).gvh.gps.getMyPosition();
				this.model_irobot = (Model_iRobot)gvh.gps.getMyPosition();
				break;
		}
		
		this.platform = platform;

		this.bridge = new RosBridge();
		bridge.connect(port, true);
	}

	public void createTopic(String topicType){
		int i = 0;
		switch(topicType){
			case "Waypoint": 
				while (i < publishers.size()){
					if (publishers.get(i).getTopic().matches("Waypoint_" + robot_name)){
						return;
					}
					i = i + 1;
				}
				Publisher pub = new Publisher(topicType + "_" + robot_name, "geometry_msgs/Point", bridge);
				publishers.add(pub);
				break;
			case "additional datatypes here.....":
				break;
		}
	}

	public void removeTopic(String topicType){										
		switch(topicType){
			case "Waypoint":
				Iterator<Publisher> it = publishers.iterator();
				while (it.hasNext()) {
					if (it.next().getTopic().matches("Waypoint_" + robot_name)) {
						it.remove();
						this.bridge.unadvertise("Waypoint_" + robot_name);
						break;
					}
				}
				break;
			case "additional datatypes here......":
				break;
		}
	
	}

	public void sendMsg(ItemPosition dest){
		int i = 0;
		while (i < publishers.size()){
			if (publishers.get(i).getTopic().matches("Waypoint_" + robot_name)){
				publishers.get(i).publish(new Point(dest.x, dest.y, dest.z));
				return;
			}
			i = i + 1;
		}
	}


	public void subscribe_to_ROS(String topic, String topicType){
		int i = 0;
			while (i < subscribed_topics.size()){
				if (subscribed_topics.get(i).matches(topic)){
					return;
				}
				i = i + 1;
			}
		switch (topicType){
			case "Waypoint":
				bridge.subscribe(SubscriptionRequestMsg.generate(topic)
					.setType("geometry_msgs/Point")
					.setThrottleRate(1000)
					.setQueueLength(0),
				new RosListenDelegate() {

					public void receive(JsonNode data, String stringRep) {
						MessageUnpacker<Point> unpacker = new MessageUnpacker<Point>(Point.class);
						Point msg = unpacker.unpackRosMessage(data);
						/*model.TESTX = msg.x;
						if(robot_name.matches("iRobot0")){
							System.out.println("Updated " + robot_name + "'s value to: " + model.TESTX);
						}

									//System.out.println(msg.x + " " + msg.y + " " +msg.z + " " + robot_name);*/
							
					}
				}
				);
				break;

			case "Reached Message":
				bridge.subscribe(SubscriptionRequestMsg.generate("/Reached")
					.setType("std_msgs/String")
					.setThrottleRate(1)
					.setQueueLength(0),
				new RosListenDelegate() {

					public void receive(JsonNode data, String stringRep) {
						MessageUnpacker<PrimitiveMsg<String>> unpacker = new MessageUnpacker<PrimitiveMsg<String>>(PrimitiveMsg.class);
						PrimitiveMsg<String> msg = unpacker.unpackRosMessage(data);
						//ADD STUFF HERE
						switch(msg.data){
							case "TRUE":
								switch(platform){
									case "Car":
										model_car.reached = true;
										break;
								}
								break;
							case "FALSE":
								switch(platform){
									case "Car":
										model_car.reached = false;
										break;
								}
								break;
						}
					}
				}
		);

		}


	}
	


}

















