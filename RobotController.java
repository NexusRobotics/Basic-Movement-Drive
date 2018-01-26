package org.usfirst.frc.team5787.robot.basicmovementdrive;

import java.util.ArrayList;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class RobotController {
	public enum TaskType{
		MOVE, ROTATE_L, ROTATE_R, PICKUP, PLACE
	};
	public DifferentialDrive drive;
	public float taskProgress = 0;
	public float prevValue;
	public AHRS ahrs;
	public ArrayList<Task> taskQueue = new ArrayList<Task>();
	public void update() {
		if (taskProgress <= 0) {
			drive.tankDrive(0, 0);
			taskQueue.remove(0);
			taskProgress = taskQueue.get(0).amount;
		}
		Task task = taskQueue.get(0);
		if (task.type == TaskType.MOVE) {
			drive.tankDrive(0.3, 0.3);
			taskProgress -= Math.abs(prevValue - getSensorProximity());
			prevValue = getSensorProximity();
		}
		else if (task.type == TaskType.ROTATE_L) {
			drive.tankDrive(-0.3, 0.3);
			taskProgress -= Math.abs(prevValue - ahrs.getYaw());
			prevValue = ahrs.getYaw();
		}
		else if (task.type == TaskType.ROTATE_R) {
			drive.tankDrive(0.3, -0.3);
			taskProgress -= Math.abs(prevValue - ahrs.getYaw());
			prevValue = ahrs.getYaw();
		}
	}
	public int getSensorProximity() {
		return 0;
	}
	public class Task{
		public TaskType type;
		public float amount;
		public Task(TaskType type, float amount) {
			this.type = type;
			this.amount = amount;
		}
	}
}
