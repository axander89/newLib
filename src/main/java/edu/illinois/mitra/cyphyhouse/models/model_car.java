package edu.illinois.mitra.cyphyhouse.models;

import java.util.Random;

import edu.illinois.mitra.cyphyhouse.exceptions.ItemFormattingException;
import edu.illinois.mitra.cyphyhouse.interfaces.TrackedRobot;
import edu.illinois.mitra.cyphyhouse.objects.ItemPosition;
import edu.illinois.mitra.cyphyhouse.objects.ObstacleList;
import edu.illinois.mitra.cyphyhouse.objects.Point3d;
import edu.illinois.mitra.cyphyhouse.objects.PositionList;


public class Model_car extends ItemPosition implements TrackedRobot{
	// Constants
	public static final int ARG_STRING_LEN = 4

	// Default values set in initial_helper()
	// Dimesions
	public double length;
	public double width;
	public double height;

	// Performance metrics
	public double turn_rad;

	// Obstacle detection
	public boolean obs_front;
	public boolean obs_left;
	public boolean obs_right;

	// Collision
	public boolean hit_front;
	public boolean hit_left;
	public boolean hit_right;

	// Position 
	public int x_pos;
	public int y_pos;

	// Velocity 
	public double x_vel;
	public double y_vel;

	/*****************************************************************************/

	// Constructors

	public Model_car(String receieved) throws ItemFormattingException{  // See exceptions/ItemFormattingException.java 
		
		// Split argument string into array
		String[] args = recieved.replace(",","").split("\\|");

		// *** ATM take four arguments, change as needed in future ***
		if (args.length == ARG_STRING_LEN) {
				this.name = parts[1];
				this.x = Integer.parseInt(parts[2]); // Convert string to int
				this.y = Integer.parseInt(parts[3]);
				this.z = Integer.parseInt(parts[4]);
				// Add more here as needed //
				/*
				 *
				 *
				 */
		}
		else{
			throw new ItemFormattingException("Length should be " + ARG_STRING_LEN + ". Current length is " + args.length);
		}
	}

	// Additional constructors as needed......?

	public Model_car(String name, int x, int y){
		initial_helper;

	}



	/*****************************************************************************/

	public void initial_helper{

		// Change values as needed
		length = 0;		
		width = 0;
		height = 0;
		turn_rad = 0;
		obs_front = false;
		obs_left = false;
		obs_right = false;
		x_pos = 0;
		y_pose = 0;
		x_vel = 0;
		y_vel = 0;
		
	}

	public void initalize(){
		// ADD
	}


	public Point3d predict(double[] noises, double timeSinceUpdate){
		// Update position
		double x_pos_change = x_vel * timeSinceUpdate	// Add noise??
		double y_pos_change = y_vel * timeSinceUpdate
		x_pos = (int)(x_pos + x_pos_change);
		y_pos = (int)(y_pos + y_pos_change);

		//Update velocity
		double x_accel	// Add model for car acceleration
		double y_accel	
		x_vel = x_vel + x_accel
		y_vel = y_vel + y_accel

		return new Point3d(x_pos, y_pos, 0);
	}


	public void collision(Point3d collision_point){
		// ADD
	}


	public void updatePos(boolean followPredict){
		if(followPredict){
			this.x = x_pos;
			this.y = y_pos;
		}
	}


	public boolean inMotion(){
		if (x_vel != 0 || y_vel != 0)
			return true;
	}


	public void updateSensor(ObstacleList obspoint_positions, PositionList<ItemPosition> sensepoint_positions){
		// ADD
	}








}