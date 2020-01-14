import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import ca.mcgill.ecse223.block.model.*;
import ca.mcgill.ecse223.block.model.BouncePoint.BounceDirection;

public class Demo {
	public static final double blockSize = 20;
	public static final double ballRadius = 5;
	public static final double blockX = 210;
	public static final double blockY = 10;
	public static final double currentBallX = 205;
	public static final double currentBallY = 5;
	public static final double ballDirectionX =2;
	public static final double ballDirectionY =4.5;

	public static void main(String[] args) {
		BouncePoint bouncePoint = new BouncePoint(0, 0, null);
		BouncePoint A = new BouncePoint(0, 0, null);
		BouncePoint B = new BouncePoint(0, 0, null);
		BouncePoint C = new BouncePoint(0, 0, null);
		BouncePoint D = new BouncePoint(0, 0, null);
		BouncePoint E = new BouncePoint(0, 0, null);
		BouncePoint F = new BouncePoint(0, 0, null);
		BouncePoint G = new BouncePoint(0, 0, null);
		BouncePoint H = new BouncePoint(0, 0, null);
		Rectangle2D rect = new Rectangle2D.Double(blockX - ballRadius, blockY - ballRadius, blockSize + 2 * ballRadius,
				blockSize + 2 * ballRadius);
		if (rect.intersectsLine(currentBallX, currentBallY, currentBallX + ballDirectionX,
				currentBallY + ballDirectionY) == false) {
			System.out.println("NO BOUNCE POINT");
			System.out.println("Misses the big rectangle");
			System.out.println("X= " + bouncePoint.getX());
			System.out.println("Y= " + bouncePoint.getY());
			System.out.println(bouncePoint.getDirection());
			return;
		} else {
			ArrayList<BouncePoint> bps = new ArrayList<BouncePoint>(8);
			A.setX(getLineSegmentsIntersctionX(currentBallX, currentBallY, currentBallX + ballDirectionX,
					currentBallY + ballDirectionY, blockX, blockY - ballRadius, blockX + blockSize,
					blockY - ballRadius));
			A.setY(getLineSegmentsIntersctionY(currentBallX, currentBallY, currentBallX + ballDirectionX,
					currentBallY + ballDirectionY, blockX, blockY - ballRadius, blockX + blockSize,
					blockY - ballRadius));
			A.setDirection(getLineSegmentsIntersctionBounceDirection(currentBallX, currentBallY,
					currentBallX + ballDirectionX, currentBallY + ballDirectionY, blockX, blockY - ballRadius,
					blockX + blockSize, blockY - ballRadius));
			bps.add(A);
			B.setX(getLineSegmentsIntersctionX(currentBallX, currentBallY, currentBallX + ballDirectionX,
					currentBallY + ballDirectionY, blockX - ballRadius, blockY, blockX - ballRadius,
					blockY + blockSize));
			B.setY(getLineSegmentsIntersctionY(currentBallX, currentBallY, currentBallX + ballDirectionX,
					currentBallY + ballDirectionY, blockX - ballRadius, blockY, blockX - ballRadius,
					blockY + blockSize));
			B.setDirection(getLineSegmentsIntersctionBounceDirection(currentBallX, currentBallY,
					currentBallX + ballDirectionX, currentBallY + ballDirectionY, blockX - ballRadius, blockY,
					blockX - ballRadius, blockY + blockSize));
			bps.add(B);
			C.setX(getLineSegmentsIntersctionX(currentBallX, currentBallY, currentBallX + ballDirectionX,
					currentBallY + ballDirectionY, blockX + blockSize + ballRadius, blockY,
					blockX + blockSize + ballRadius, blockY + blockSize));
			C.setY(getLineSegmentsIntersctionY(currentBallX, currentBallY, currentBallX + ballDirectionX,
					currentBallY + ballDirectionY, blockX + blockSize + ballRadius, blockY,
					blockX + blockSize + ballRadius, blockY + blockSize));
			C.setDirection(getLineSegmentsIntersctionBounceDirection(currentBallX, currentBallY,
					currentBallX + ballDirectionX, currentBallY + ballDirectionY, blockX + blockSize + ballRadius,
					blockY, blockX + blockSize + ballRadius, blockY + blockSize));
			bps.add(C);
			D.setX(getLineSegmentsIntersctionX(currentBallX, currentBallY, currentBallX + ballDirectionX,
					currentBallY + ballDirectionY, blockX, blockY + blockSize + ballRadius, blockX + blockSize,
					blockY + blockSize + ballRadius));
			D.setY(getLineSegmentsIntersctionY(currentBallX, currentBallY, currentBallX + ballDirectionX,
					currentBallY + ballDirectionY, blockX, blockY + blockSize + ballRadius, blockX + blockSize,
					blockY + blockSize + ballRadius));
			D.setDirection(getLineSegmentsIntersctionBounceDirection(currentBallX, currentBallY,
					currentBallX + ballDirectionX, currentBallY + ballDirectionY, blockX,
					blockY + blockSize + ballRadius, blockX + blockSize, blockY + blockSize + ballRadius));
			bps.add(D);
			E.setX(getLineQuarterCircleSegmentsIntersctionX(currentBallX, currentBallY, currentBallX + ballDirectionX,
					currentBallY + ballDirectionY, blockX - ballRadius, blockY, blockX, blockY - ballRadius));
			E.setY(getLineQuarterCircleSegmentsIntersctionY(currentBallX, currentBallY, currentBallX + ballDirectionX,
					currentBallY + ballDirectionY, blockX - ballRadius, blockY, blockX, blockY - ballRadius));
			E.setDirection(getLineQuarterCircleSegmentsIntersctionBlockLeftSideBounceDirection(currentBallX, currentBallY, currentBallX + ballDirectionX,
					currentBallY + ballDirectionY, blockX - ballRadius, blockY, blockX, blockY - ballRadius));
			bps.add(E);
			F.setX(getLineQuarterCircleSegmentsIntersctionX(currentBallX, currentBallY, currentBallX + ballDirectionX,
					currentBallY + ballDirectionY, blockX + blockSize + ballRadius, blockY, blockX + blockSize,
					blockY - ballRadius));
			F.setY(getLineQuarterCircleSegmentsIntersctionY(currentBallX, currentBallY, currentBallX + ballDirectionX,
					currentBallY + ballDirectionY, blockX + blockSize + ballRadius, blockY, blockX + blockSize,
					blockY - ballRadius));
			F.setDirection(getLineQuarterCircleSegmentsIntersctionBlockRightSideBounceDirection(currentBallX, currentBallY, currentBallX + ballDirectionX,
					currentBallY + ballDirectionY, blockX + blockSize + ballRadius, blockY, blockX + blockSize,
					blockY - ballRadius));
			bps.add(F);
			G.setX(getLineQuarterCircleSegmentsIntersctionX(currentBallX, currentBallY, currentBallX + ballDirectionX,
					currentBallY + ballDirectionY, blockX - ballRadius, blockY + blockSize, blockX,
					blockY + blockSize + ballRadius));
			G.setY(getLineQuarterCircleSegmentsIntersctionY(currentBallX, currentBallY, currentBallX + ballDirectionX,
					currentBallY + ballDirectionY, blockX - ballRadius, blockY + blockSize, blockX,
					blockY + blockSize + ballRadius));
			G.setDirection(getLineQuarterCircleSegmentsIntersctionBlockLeftSideBounceDirection(currentBallX, currentBallY, currentBallX + ballDirectionX,
					currentBallY + ballDirectionY, blockX - ballRadius, blockY + blockSize, blockX,
					blockY + blockSize + ballRadius));
			bps.add(G);
			H.setX(getLineQuarterCircleSegmentsIntersctionX(currentBallX, currentBallY, currentBallX + ballDirectionX,
					currentBallY + ballDirectionY, blockX + blockSize + ballRadius, blockY + blockSize,
					blockX + blockSize, blockY + blockSize + ballRadius));
			H.setY(getLineQuarterCircleSegmentsIntersctionY(currentBallX, currentBallY, currentBallX + ballDirectionX,
					currentBallY + ballDirectionY, blockX + blockSize + ballRadius, blockY + blockSize,
					blockX + blockSize, blockY + blockSize + ballRadius));
			H.setDirection(getLineQuarterCircleSegmentsIntersctionBlockRightSideBounceDirection(currentBallX, currentBallY, currentBallX + ballDirectionX,
					currentBallY + ballDirectionY, blockX + blockSize + ballRadius, blockY + blockSize,
					blockX + blockSize, blockY + blockSize + ballRadius));
			bps.add(H);
			ArrayList<BouncePoint> possibleBouncePointsOnOneBlock = new ArrayList<BouncePoint>();
			for (int i = 0; i < 8; i++) {
				// (0,0) is impossible to be the bounce point, thus use (0,0) for null bounce
				// point
				// If x (or y) = 0, then the bounce point is null. Discard.
				if (bps.get(i).getX() != 0 || bps.get(i).getY() != 0) {
					possibleBouncePointsOnOneBlock.add(bps.get(i));
				}
			}
			if (possibleBouncePointsOnOneBlock.isEmpty()) {
				System.out.println("NO BOUNCE POINT");
				System.out.println("X= " + bouncePoint.getX());
				System.out.println("Y= " + bouncePoint.getY());
				System.out.println(bouncePoint.getDirection());
				return;
			} else if (possibleBouncePointsOnOneBlock.size() == 1) {
				bouncePoint = possibleBouncePointsOnOneBlock.get(0);
				System.out.println("FIND BOUNCE POINT!");
				System.out.println("X= " + bouncePoint.getX());
				System.out.println("Y= " + bouncePoint.getY());
				System.out.println(bouncePoint.getDirection());
				return;

			} else if (isCloser(possibleBouncePointsOnOneBlock.get(0), possibleBouncePointsOnOneBlock.get(1)) == true) {
				bouncePoint = possibleBouncePointsOnOneBlock.get(0);
				System.out.println("FIND BOUNCE POINT!");
				System.out.println("X= " + bouncePoint.getX());
				System.out.println("Y= " + bouncePoint.getY());
				System.out.println(bouncePoint.getDirection());
				return;
			} else {
				bouncePoint = possibleBouncePointsOnOneBlock.get(1);
				System.out.println("FIND BOUNCE POINT!");
				System.out.println("X= " + bouncePoint.getX());
				System.out.println("Y= " + bouncePoint.getY());
				System.out.println(bouncePoint.getDirection());
			}
		}
		return;
	}

	// If the bounce point is null (x=0,y=0), it will only be assigned a null direction.
	// For quarter circle F and H in sample solution v.4
	private static BounceDirection getLineQuarterCircleSegmentsIntersctionBlockRightSideBounceDirection(double x1, double y1, double x2, double y2,
			double x3, double y3, double x4, double y4) {
		BounceDirection bounceDirection = null;
		if (getLineQuarterCircleSegmentsIntersctionY(x1,y1,x2,y3,x3,y3,x4,y4)==0 && getLineQuarterCircleSegmentsIntersctionX(x1,y1,x2,y3,x3,y3,x4,y4)==0) {
			return null;
		}
		else if (ballDirectionX > 0) {
			bounceDirection = BounceDirection.FLIP_Y;
		} else if (ballDirectionX < 0) {
			bounceDirection = BounceDirection.FLIP_X;
		}
		return bounceDirection;
	}

	// If the bounce point is null (x=0,y=0), it will only be assigned a null direction.
	// For quarter circle E and G in sample solution v.4
	private static BounceDirection getLineQuarterCircleSegmentsIntersctionBlockLeftSideBounceDirection(double x1, double y1, double x2, double y2,
			double x3, double y3, double x4, double y4) {
		BounceDirection bounceDirection = null;
		if (getLineQuarterCircleSegmentsIntersctionY(x1,y1,x2,y3,x3,y3,x4,y4)==0 && getLineQuarterCircleSegmentsIntersctionX(x1,y1,x2,y3,x3,y3,x4,y4)==0) {
			return null;
		}
		else if (ballDirectionX > 0) {
			bounceDirection = BounceDirection.FLIP_X;
		} else if (ballDirectionX < 0) {
			bounceDirection = BounceDirection.FLIP_Y;
		}
		return bounceDirection;
	}

	 private static double getLineQuarterCircleSegmentsIntersctionY(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
		    double lineQuarterCircleSegmentsIntersctionY = 0;
			double lineCircleIntersectionX = getLineCircleIntersectionX(x1, y1, x2, y2, x3, y3, x4, y4);
			double lineCircleIntersectionY = getLineCircleIntersectionY(x1, y1, x2, y2, x3, y3, x4, y4);
			if (isOnLineSegment(lineCircleIntersectionX, lineCircleIntersectionY, x1, y1, x2, y2) == false
					|| isOnQuarterCircleSegment(lineCircleIntersectionX, lineCircleIntersectionY, x3, y3, x4,
							y4) == false) {
				return 0;
			} else {
				lineQuarterCircleSegmentsIntersctionY = lineCircleIntersectionY;
			}
			return lineQuarterCircleSegmentsIntersctionY;
		  }


		  /**
		   * If there is no intersection, return 0.
		   */
		  // line 314 "../../../../../Block223States.ump"
		   private static double getLineQuarterCircleSegmentsIntersctionX(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
		    double lineQuarterCircleSegmentsIntersctionX = 0;
			double lineCircleIntersectionX = getLineCircleIntersectionX(x1, y1, x2, y2, x3, y3, x4, y4);
			double lineCircleIntersectionY = getLineCircleIntersectionY(x1, y1, x2, y2, x3, y3, x4, y4);
			if (isOnLineSegment(lineCircleIntersectionX, lineCircleIntersectionY, x1, y1, x2, y2) == false
					|| isOnQuarterCircleSegment(lineCircleIntersectionX, lineCircleIntersectionY, x3, y3, x4,
							y4) == false) {
				return 0;
			} else {
				lineQuarterCircleSegmentsIntersctionX = lineCircleIntersectionX;
			}
			return lineQuarterCircleSegmentsIntersctionX;
		  }

	/*
	 * Method to determine whether the intersection point is on the quarter circle
	 * E,F,G,or H without using multiple methods: If the point is on the respective
	 * circle segment, then the line segment from center of circle (x4,y3) to
	 * (pX,pY) must have exactly one intersection with the line segment from (x3,y3)
	 * to (x4,y4). Discard the cases of (pX,pY) duplicates with (x3,y3) or (x4,y4).
	 */
	private static boolean isOnQuarterCircleSegment(double pX, double pY, double x3, double y3, double x4, double y4) {
		boolean isOnQuarterCircleSegment = false;
		if (pX == x3 || pX == x4 || pY == y3 || pY == y4) {
			return false;
		} else {
			double point_x = getLineSegmentsIntersctionX(x4, y3, pX, pY, x3, y3, x4, y4);
			double point_y = getLineSegmentsIntersctionY(x4, y3, pX, pY, x3, y3, x4, y4);
			if (point_x * point_y == 0) {
				return false;
			} else {
				isOnQuarterCircleSegment = true;
			}
		}
		return isOnQuarterCircleSegment;
	}

	// center of circle : (x4,y3)
	// If there is no intersection, return 0.
	private static double getLineCircleIntersectionY(double x1, double y1, double x2, double y2, double x3, double y3,
			double x4, double y4) {
		double ballRadius = Ball.BALL_DIAMETER / 2; // 5
		double lineCircleIntersectionY = 0;
		// case1: vertical line parallel is tangent with the circle. Discard
		if (x1 == x2 && x1 == x4 && x2 == x4) {
			return 0;
		}
		// case2: horizontal line is tangent with the circle. Discard.
		else if (y1 == y2 && y1 == y3 && y2 == y3) {
			return 0;
		}
		// case3: line and circle are away (return 0). Discard.
		else if (getDistanceFromPointToLine(x4, y3, x1, y1, x2, y2) > ballRadius) {
			return 0;
		}
		// case4: horizontal line intersects with circle. The intersection that is
		// closest to (x1,y1) is valid
		else if (y1 == y2) {
			lineCircleIntersectionY = y1;
		}
		// case5: vertical line intersects with circle. The intersection that is closest
		// to (x1,y1) is valid
		else if (x1 == x2) {
			double ponitY_1 = y3 - Math.sqrt(Math.pow(ballRadius, 2) - Math.pow((x4 - x1), 2));
			double ponitY_2 = y3 + Math.sqrt(Math.pow(ballRadius, 2) - Math.pow((x4 - x1), 2));
			if (Math.abs(y1 - ponitY_1) < Math.abs(y1 - ponitY_2)) {
				lineCircleIntersectionY = ponitY_1;
			} else {
				lineCircleIntersectionY = ponitY_2;
			}
		}
		// case6: line (non-horizontal and non-vertical) and circle are tangent
		/*
		 * The equation of the line is y= mx+c The circle: (x-p)^2 + (y-q)^2 = r^2
		 * Intersection Equation:
		 * (𝑚^2+1)𝑥^2+2(𝑚𝑐−𝑚𝑞−𝑝)𝑥+(𝑞^2−𝑟^2+𝑝^2−2𝑐𝑞+𝑐^2)=0
		 */
		else if (getDistanceFromPointToLine(x4, y3, x1, y1, x2, y2) == ballRadius) {
			double m = (y2 - y1) / (x2 - x1);
			double c = y2 - m * x2;
			double A = Math.pow(m, 2) + 1;
			double B = 2 * (m * c - m * y3 - x4);
			// double C = Math.pow(y3,2)-Math.pow(5,2)+Math.pow(x4,2)-2*c*y3+Math.pow(c,2);
			lineCircleIntersectionY = m * (-B / (2 * A)) + c; // B^2-4AC = 0
		}
		// case7: line (non-horizontal and non-vertical) and circle intersects.
		// The intersection that is closest to (x1,y1) is valid.
		else if (getDistanceFromPointToLine(x4, y3, x1, y1, x2, y2) < ballRadius) {
			double m = (y2 - y1) / (x2 - x1);
			double c = y2 - m * x2;
			double A = Math.pow(m, 2) + 1;
			double B = 2 * (m * c - m * y3 - x4);
			double C = Math.pow(y3, 2) - Math.pow(ballRadius, 2) + Math.pow(x4, 2) - 2 * c * y3 + Math.pow(c, 2);
			double point_1_X = (-B + Math.sqrt(Math.pow(B, 2) - 4 * A * C)) / (2 * A);
			double point_1_Y = m * point_1_X + c;
			double point_2_X = (-B - Math.sqrt(Math.pow(B, 2) - 4 * A * C)) / (2 * A);
			double point_2_Y = m * point_2_X + c;
			double d1 = Math.sqrt(Math.pow((x1 - point_1_X), 2) + Math.pow((y1 - point_1_Y), 2));
			double d2 = Math.sqrt(Math.pow((x1 - point_2_X), 2) + Math.pow((y1 - point_2_Y), 2));
			if (d1 < d2) {
				lineCircleIntersectionY = point_1_Y;
			} else {
				lineCircleIntersectionY = point_2_Y;
			}
		}
		return lineCircleIntersectionY;
	}

	// center of circle : (x4,y3)
	// If there is no intersection, return 0.
	private static double getLineCircleIntersectionX(double x1, double y1, double x2, double y2, double x3, double y3,
			double x4, double y4) {
		double ballRadius = Ball.BALL_DIAMETER / 2; // 5
		double lineCircleIntersectionX = 0;
		// case1: vertical line parallel is tangent with the circle. Discard
		if (x1 == x2 && x1 == x4 && x2 == x4) {
			return 0;
		}
		// case2: horizontal line is tangent with the circle. Discard.
		else if (y1 == y2 && y1 == y3 && y2 == y3) {
			return 0;
		}
		// case3: line and circle are away (return 0). Discard.
		else if (getDistanceFromPointToLine(x4, y3, x1, y1, x2, y2) > ballRadius) {
			return 0;
		}
		// case4: horizontal line intersects with circle. The intersection that is
		// closest to (x1,y1) is valid
		else if (y1 == y2) {
			double ponitX_1 = x4 - Math.sqrt(Math.pow(ballRadius, 2) - Math.pow((y3 - y1), 2));
			double ponitX_2 = x4 + Math.sqrt(Math.pow(ballRadius, 2) - Math.pow((y3 - y1), 2));
			if (Math.abs(x1 - ponitX_1) < Math.abs(x1 - ponitX_2)) {
				lineCircleIntersectionX = ponitX_1;
			} else {
				lineCircleIntersectionX = ponitX_2;
			}
		}
		// case5: vertical line intersects with circle. The intersection that is closest
		// to (x1,y1) is valid
		else if (x1 == x2) {
			lineCircleIntersectionX = x1;

		}
		// case6: line (non-horizontal and non-vertical) and circle are tangent
		/*
		 * The equation of the line is y= mx+c The circle: (x-p)^2 + (y-q)^2 = r^2
		 * Intersection Equation:
		 * (𝑚^2+1)𝑥^2+2(𝑚𝑐−𝑚𝑞−𝑝)𝑥+(𝑞^2−𝑟^2+𝑝^2−2𝑐𝑞+𝑐^2)=0
		 */
		else if (getDistanceFromPointToLine(x4, y3, x1, y1, x2, y2) == ballRadius) {
			double m = (y2 - y1) / (x2 - x1);
			double c = y2 - m * x2;
			double A = Math.pow(m, 2) + 1;
			double B = 2 * (m * c - m * y3 - x4);
			// double C = Math.pow(y3,2)-Math.pow(5,2)+Math.pow(x4,2)-2*c*y3+Math.pow(c,2);
			lineCircleIntersectionX = -B / (2 * A); // B^2-4AC = 0
		}
		// case7: line (non-horizontal and non-vertical) and circle intersects.
		// The intersection that is closest to (x1,y1) is valid.
		else if (getDistanceFromPointToLine(x4, y3, x1, y1, x2, y2) < ballRadius) {
			double m = (y2 - y1) / (x2 - x1);
			double c = y2 - m * x2;
			double A = Math.pow(m, 2) + 1;
			double B = 2 * (m * c - m * y3 - x4);
			double C = Math.pow(y3, 2) - Math.pow(ballRadius, 2) + Math.pow(x4, 2) - 2 * c * y3 + Math.pow(c, 2);
			double point_1_X = (-B + Math.sqrt(Math.pow(B, 2) - 4 * A * C)) / (2 * A);
			double point_1_Y = m * point_1_X + c;
			double point_2_X = (-B - Math.sqrt(Math.pow(B, 2) - 4 * A * C)) / (2 * A);
			double point_2_Y = m * point_2_X + c;
			double d1 = Math.sqrt(Math.pow((x1 - point_1_X), 2) + Math.pow((y1 - point_1_Y), 2));
			double d2 = Math.sqrt(Math.pow((x1 - point_2_X), 2) + Math.pow((y1 - point_2_Y), 2));
			if (d1 < d2) {
				lineCircleIntersectionX = point_1_X;
			} else {
				lineCircleIntersectionX = point_2_X;
			}
		}
		return lineCircleIntersectionX;
	}

	/*
	 * The equation of the line ax + by + c = 0. The coordinate of the point is (x1,
	 * y1) The formula for distance between the point and the line in 2-D is given
	 * by: Distance = (| a*x1 + b*y1 + c |) / (sqrt( a*a + b*b))
	 */
	private static double getDistanceFromPointToLine(double x4, double y3, double x1, double y1, double x2, double y2) {
		// line: ax+by+c=0
		double a = y2 - y1;
		double b = x1 - x2;
		double c = x2 * y1 - x1 * y2;
		double distance = Math.abs(((a * x4 + b * y3 + c)) / (Math.sqrt(a * a + b * b)));
		return distance;
	}

	// If the bounce point is null (x=0,y=0), it will only be assigned a null direction.
	private static BounceDirection getLineSegmentsIntersctionBounceDirection(double x1, double y1, double x2, double y2,
			double x3, double y3, double x4, double y4) {
		BounceDirection bounceDirection = null;
		if (getLineSegmentsIntersctionY(x1,y1,x2,y2,x3,y3,x4,y4)==0 && getLineSegmentsIntersctionX(x1,y1,x2,y2,x3,y3,x4,y4)==0) {
			return null;
		}
		// for line A and line D in sample solution v.4
		else if (y3 == y4) {
			bounceDirection = BounceDirection.FLIP_Y;
		}
		// for line B and line C in sample solution v.4
		else if (x3 == x4) {
			bounceDirection = BounceDirection.FLIP_X;
		}
		return bounceDirection;
	}

	// If there is no intersection, return 0.
	private static double getLineSegmentsIntersctionY(double x1, double y1, double x2, double y2, double x3, double y3,
			double x4, double y4) {

		double lineSegmentsIntersctionY = 0;
		boolean end_1 = isOnLineSegment(x1,y1,x3,y3,x4,y4);
		boolean end_2 = isOnLineSegment(x2,y2,x3,y3,x4,y4);
		boolean end_3 = isOnLineSegment(x3,y3,x1,y1,x2,y2);
		boolean end_4 = isOnLineSegment(x4,y4,x1,y1,x2,y2);
		if (end_1) {
			return y1;
		}
		if (end_2) {
			return y2;
		}
		if (end_3) {
			return y3;
		}
		if (end_4) {
			return y4;
		}
		
		else if (isparallel(x1, y1, x2, y2, x3, y3, x4, y4)) {
			return 0;
		} 
		
			else {
			double linesIntersectionX = getLinesIntersectionX(x1, y1, x2, y2, x3, y3, x4, y4);
			double linesIntersectionY = getLinesIntersectionY(x1, y1, x2, y2, x3, y3, x4, y4);
			if (isOnLineSegment(linesIntersectionX, linesIntersectionY, x1, y1, x2, y2) == false
					|| isOnLineSegment(linesIntersectionX, linesIntersectionY, x3, y3, x4, y4) == false) {
				return 0;
			} else {
				lineSegmentsIntersctionY = linesIntersectionY;
			}
		}
		return lineSegmentsIntersctionY;
	}

	// If there is no intersection, return 0.
	private static double getLineSegmentsIntersctionX(double x1, double y1, double x2, double y2, double x3, double y3,
			double x4, double y4) {
		double lineSegmentsIntersctionX = 0;
		boolean end_1 = isOnLineSegment(x1,y1,x3,y3,x4,y4);
		boolean end_2 = isOnLineSegment(x2,y2,x3,y3,x4,y4);
		boolean end_3 = isOnLineSegment(x3,y3,x1,y1,x2,y2);
		boolean end_4 = isOnLineSegment(x4,y4,x1,y1,x2,y2);
		if (end_1) {
			return x1;
		}
		if (end_2) {
			return x2;
		}
		if (end_3) {
			return x3;
		}
		if (end_4) {
			return x4;
		}
		
		else if (isparallel(x1, y1, x2, y2, x3, y3, x4, y4)) {
			return 0;
		}
		if (isparallel(x1, y1, x2, y2, x3, y3, x4, y4)) {
			return 0;
		} else {
			double linesIntersectionX = getLinesIntersectionX(x1, y1, x2, y2, x3, y3, x4, y4);
			double linesIntersectionY = getLinesIntersectionY(x1, y1, x2, y2, x3, y3, x4, y4);
			if (isOnLineSegment(linesIntersectionX, linesIntersectionY, x1, y1, x2, y2) == false
					|| isOnLineSegment(linesIntersectionX, linesIntersectionY, x3, y3, x4, y4) == false) {
				return 0;
			} else {
				lineSegmentsIntersctionX = linesIntersectionX;
			}
		}
		return lineSegmentsIntersctionX;
	}

	private static boolean isOnLineSegment(double pX, double pY, double x1, double y1, double x2, double y2) {
		boolean isOnLineSegment = false;
		if (x1 == x2 && y1 == y2) {
			return false;
		}
		// vertical line segment
		else if (x1 == x2) {
			double topEndPointY = Math.max(y1, y2);
			double bottomEndPointY = Math.min(y1, y2);
			if (pX == x1 && pY <= topEndPointY && pY >= bottomEndPointY) {
				isOnLineSegment = true;
			}
		}
		// horizontal line segment
		else if (y1 == y2) {
			double leftEndPointX = Math.min(x1, x2);
			double rightEndPointX = Math.max(x1, x2);
			if (pY == y1 && pX >= leftEndPointX && pX <= rightEndPointX) {
				isOnLineSegment = true;
			}
		}
		// line segment with negative slope
		else if ((y2 - y1) / (x2 - x1) < 0) {
			double upperLeftEndPointX = Math.min(x1, x2);
			double upperLeftEndPointY = Math.max(y1, y2);
			double lowerRightEndPointX = Math.max(x1, x2);
			double lowerRightEndPointY = Math.min(y1, y2);
			if (pX >= upperLeftEndPointX && pX <= lowerRightEndPointX && pY <= upperLeftEndPointY
					&& pY >= lowerRightEndPointY) {
				isOnLineSegment = true;
			}
		}
		// line segment with positive slope
		else if ((y2 - y1) / (x2 - x1) > 0) {
			double upperRightEndPointX = Math.max(x1, x2);
			double upperRightEndPointY = Math.max(y1, y2);
			double lowerLeftEndPointX = Math.min(x1, x2);
			double lowerLeftEndPointY = Math.min(y1, y2);
			if (pX >= lowerLeftEndPointX && pX <= upperRightEndPointX && pY >= lowerLeftEndPointY
					&& pY <= upperRightEndPointY) {
				isOnLineSegment = true;
			}
		}
		return isOnLineSegment;
	}

	private static double getLinesIntersectionY(double x1, double y1, double x2, double y2, double x3, double y3,
			double x4, double y4) {
		double intersectionY;
		if (isparallel(x1, y1, x2, y2, x3, y3, x4, y4)) {
			return 0;
		} else {
			intersectionY = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4))
					/ ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
		}
		return intersectionY;
	}

	private static double getLinesIntersectionX(double x1, double y1, double x2, double y2, double x3, double y3,
			double x4, double y4) {
		double intersectionX;
		if (isparallel(x1, y1, x2, y2, x3, y3, x4, y4)) {
			return 0;
		} else {
			intersectionX = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4))
					/ ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
		}
		return intersectionX;
	}

	private static boolean isparallel(double x1, double y1, double x2, double y2, double x3, double y3, double x4,
			double y4) {
		if (((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4)) == 0) {
			return true;
		}
		return false;
	}

	private static boolean isCloser(BouncePoint first, BouncePoint second) {
		boolean isCloser = false;
		if (first == null) {
			isCloser = false;
		} else if (second == null) {
			isCloser = true;
		} else {
			double firstX = first.getX();
			double firstY = first.getY();
			double secondX = second.getX();
			double secondY = second.getY();
			double distanceBallFirst = Math
					.sqrt(Math.pow((firstX - currentBallX), 2) + Math.pow((firstY - currentBallY), 2));
			double distanceBallSecond = Math
					.sqrt(Math.pow((secondX - currentBallX), 2) + Math.pow((secondY - currentBallY), 2));
			if (distanceBallFirst < distanceBallSecond) {
				isCloser = true;
			}
		}
		return isCloser;
	}

}
