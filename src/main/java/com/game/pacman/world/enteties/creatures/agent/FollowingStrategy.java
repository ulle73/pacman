package com.game.pacman.world.enteties.creatures.agent;

import java.util.List;

import com.game.pacman.world.enteties.creatures.agent.pathfinder.PathFinder;

public class FollowingStrategy extends Strategy {
	
	protected PathFinder astar;
	protected Agent agent;

	protected List<Integer> path;
	protected int pathPosition;

	protected int targetCell;
	protected int targetX;
	protected int targetY;
	

	public FollowingStrategy(final int[][] matrix, PathFinder astar) {
		super.setMatrix(matrix);
		this.astar = astar;
		pathPosition = 0;
	}
	
	@Override
	public void findPath(int currentX, int currentY, int playerX, int playerY) {
		targetX = currentX; // until path has been calculated
		targetY = currentY;
		int currentCell = currentX + (currentY * super.width) + 1;
		if(getPath() == null && !astar.isProcessing()) {
			process(currentX, currentY, playerX, playerY);
		} else {
			if(getPath() != null && pathPosition < getPath().size()) {
				targetCell = getPath().get(pathPosition);
				if(targetCell == currentCell && pathPosition != getPath().size()-1) {  // get next target from path
					targetCell = getPath().get(++pathPosition);
				}
				targetX = getXCoord(targetCell);
				targetY = getYCoord(targetCell);
			} else {
				pathPosition = 0;
				if(getPath() != null) // reached end => Switch to bread crumb strategy
					agent.setStrategy(new RandomStrategy());
//					agent.setStrategy(new BreadCrumbsStrategy(matrix));
			}
		}
	}
	
	
	protected void process(int currentX, int currentY, int playerX, int playerY) {
		setPath(astar.calculatePath(currentX, currentY, playerX, playerY));
	}

	protected void setPath(List<Integer> path) {
		this.path = path;
	}
	
	private List<Integer> getPath() {
		return path;
	}

	@Override
	public int getXDir(int currentX, int currentY) {
		return (targetX > (int) currentX) ? 1 : ((targetX < (int) currentX) ? -1 : 0);   
	}

	int getXCoord(int cell) {
		return (cell-1) % super.width;
	}

	@Override
	public int getYDir(int currentX, int currentY) {
		return (targetY > (int) currentY) ? 1 : ((targetY < (int) currentY) ? -1 : 0);   
	}

	int getYCoord(int cell) {
		return (cell-1) / super.width;
	}

	@Override
	public void setAgent(Agent agent) {
		this.agent = agent;
	}

}
