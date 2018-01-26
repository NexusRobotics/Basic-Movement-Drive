package org.usfirst.frc.team5787.robot.basicmovementdrive;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class MovementDrive extends IterativeRobot implements PIDOutput{
	enum Configuration{
		TANK, ARCADE
	};
	final double TURN_SPEED = 1.5D;
	private DifferentialDrive m_myRobot;
	private SpeedControllerGroup left, right;
	private Joystick m_leftStick;
	private XboxController xbox;
	private Timer timer;
	private Configuration config = Configuration.TANK;
	float speed = 0.3f;
	PIDController turnController;
	AHRS ahrs;
	static final double kP = 0.03;
    static final double kI = 0.00;
    static final double kD = 0.00;
    static final double kF = 0.00;
    
    static final double kToleranceDegrees = 2.0f;

	@Override
	public void robotInit() {
		left = new SpeedControllerGroup(new Spark(0), new Spark(1));
		right = new SpeedControllerGroup(new Spark(2), new Spark(3));
		
		
		m_myRobot = new DifferentialDrive(left, right);
		m_leftStick = new Joystick(0);
		xbox = new XboxController(0);
		timer = new Timer();
		try {
            ahrs = new AHRS(SPI.Port.kMXP); 
        } catch (RuntimeException ex ) {
            DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
        }
		turnController = new PIDController(kP, kI, kD, kF, ahrs, this);
        turnController.setInputRange(-180.0f,  180.0f);
        turnController.setOutputRange(-1.0, 1.0);
        turnController.setAbsoluteTolerance(kToleranceDegrees);
        turnController.setContinuous(true);
	}

	@Override
	public void teleopPeriodic() {
		boolean turbo = xbox.getBumper(Hand.kLeft);
		speed = turbo ? 1 : 0.3f;
		if (xbox.getYButtonPressed()){
			if (config == Configuration.TANK){
				config = Configuration.ARCADE;
			}
			else{
				config = Configuration.TANK;
			}
		}
		if (config == Configuration.TANK){
			m_myRobot.tankDrive(xbox.getRawAxis(1) * speed, xbox.getRawAxis(5) * speed);
		}
		else if (config == Configuration.ARCADE){
			m_myRobot.tankDrive((m_leftStick.getY() + m_leftStick.getX() / TURN_SPEED) * speed, (m_leftStick.getY() - m_leftStick.getX() / TURN_SPEED) * speed);
		}
	}
	
	public void autonomousPeriodic() { //This method is called each time the robot recieves a packet instructing the robot to be in autonomous enabled mode
	     // Drive for 2 seconds
	     if (timer.get() < 2.0) {
	          m_myRobot.tankDrive(0.3, 0.3); // drive forwards half speed
	     } else {
	          m_myRobot.tankDrive(0.0, 0.0); // stop robot
	     }
	}

	@Override
	public void pidWrite(double output) {
		// TODO Auto-generated method stub
		
	}
}
