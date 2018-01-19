package org.usfirst.frc.team5787.robot.basicmovementdrive;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class MovementDrive extends IterativeRobot {
	enum Configuration{
		TANK, ARCADE
	};
	final double TURN_SPEED = 1.5D;
	private DifferentialDrive m_myRobot;
	private DifferentialDrive m_myRobot2;
	private Joystick m_leftStick;
	private XboxController xbox;
	private Timer timer;
	private Configuration config = Configuration.TANK;

	@Override
	public void robotInit() {
		m_myRobot = new DifferentialDrive(new Spark(0), new Spark(2));
		m_myRobot2 = new DifferentialDrive(new Spark(1), new Spark(3));
		m_leftStick = new Joystick(0);
		xbox = new XboxController(0);
		timer = new Timer();
	}

	@Override
	public void teleopPeriodic() {
		if (xbox.getYButtonPressed()){
			if (config == Configuration.TANK){
				config = Configuration.ARCADE;
			}
			else{
				config = Configuration.TANK;
			}
		}
		if (config == Configuration.TANK){
			m_myRobot.tankDrive(xbox.getRawAxis(1), xbox.getRawAxis(5));
			m_myRobot2.tankDrive(xbox.getRawAxis(1), xbox.getRawAxis(5));
		}
		else if (config == Configuration.ARCADE){
			m_myRobot.tankDrive(m_leftStick.getY() + m_leftStick.getX() / TURN_SPEED, m_leftStick.getY() - m_leftStick.getX() / TURN_SPEED);
			m_myRobot2.tankDrive(m_leftStick.getY() + m_leftStick.getX() / TURN_SPEED, m_leftStick.getY() - m_leftStick.getX() / TURN_SPEED);
		}
	}
	
	public void autonomousPeriodic() { //This method is called each time the robot recieves a packet instructing the robot to be in autonomous enabled mode
	     // Drive for 2 seconds
	     if (timer.get() < 2.0) {
	          m_myRobot.tankDrive(0.5, 0.5); // drive forwards half speed
	     } else {
	          m_myRobot.tankDrive(0.0, 0.0); // stop robot
	     }
	}
}
