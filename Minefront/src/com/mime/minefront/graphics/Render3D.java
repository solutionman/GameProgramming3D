package com.mime.minefront.graphics;

import java.util.Random;

import com.mime.minefront.Game;
import com.mime.minefront.input.Controller;

public class Render3D extends Render {
	
	public double[] zBuffer;
	private double renderDistance = 5000;
	private double forward, right, up, cosine, sine, walking;


	public Render3D(int width, int height) {
		super(width, height);
		zBuffer = new double[width * height];
	}
	
	
	public void floor(Game game) {
		
		double floorPosition = 8;
		double ceilingPozition = 8;
		forward = game.controls.z;
		right = game.controls.x;
		up = game.controls.y;	
		walking = 0;
		double rotation = game.controls.rotation;
		cosine = Math.cos(rotation);
		sine = Math.sin(rotation);
		
		for (int y = 0; y < height; y++) {
			double ceiling = (y + -height / 2.0) / height;
			double z = (floorPosition + up) / ceiling;	
			
			if (Controller.walk) {
				walking = Math.sin(game.time / 6.0) * 0.5;
				z = (floorPosition + up + walking) / ceiling;
			}
			if (Controller.crouchWalk && Controller.walk) {
				walking = Math.sin(game.time / 6.0) * 0.25;
				z = (floorPosition + up + walking) / ceiling;
			}
			if (Controller.runWalk && Controller.walk) {
				walking = Math.sin(game.time / 6.0) * 0.8;
				z = (floorPosition + up + walking) / ceiling;
			}
						
			if (ceiling < 0) {
				z = (ceilingPozition - up) / - ceiling;
				if (Controller.walk) {
					z = (ceilingPozition - up - walking) / - ceiling;
				}
			}
			
			

			
			for (int x = 0; x < width; x++) {
				double depth = (x - width / 2.0) / height;
				depth *= z;
				double xx = depth * cosine + z * sine;
				double yy = z * cosine - depth * sine;
				int xPix = (int) (xx + right);
				int yPix = (int) (yy + forward);
				zBuffer[x + y * width] = z;
				pixels[x + y * width] = Texture.floor.pixels[(xPix & 7) + (yPix & 7) * 8];
				
				if (z > 500) {
					pixels[x + y * width] = 0;
				}
				
			}
		}
		
	}
	
	public void renderWall(double xLeft, double xRight, double zDistanceLeft, double zDistanceRight, double yHeight) {
		double upCorrect = 0.062;
		double rightCorrect = 0.062;
		double forwardCorrect = 0.062;
		double walkCorrect = -0.062;
		
		double xcLeft = ((xLeft) - (right * rightCorrect)) * 2;
		double zcLeft = ((zDistanceLeft) - (forward * forwardCorrect)) * 2;
		
		double rotLeftSideX = xcLeft * cosine - zcLeft * sine;
		double yCornerTL = ((-yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;
		double yCornerBL = ((+0.5 - yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;
		double rotLeftSideZ = zcLeft * cosine + xcLeft * sine;
		
		double xcRight = ((xRight) - (right * rightCorrect)) * 2;
		double zcRight = ((zDistanceRight) - (forward * forwardCorrect)) * 2;
		
		double rotRightSideX = xcRight * cosine - zcRight * sine;
		double yCornerTR = ((-yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;
		double yCornerBR = ((+0.5 - yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;
		double rotRightSideZ = zcRight * cosine + xcRight * sine;
		
		double xPixelLeft = (rotLeftSideX / rotLeftSideZ * height + width / 2);
		double xPixelRight = (rotRightSideX / rotRightSideZ * height + width / 2);
		
		if (xPixelLeft >= xPixelRight) {
			return;
		}
		
		int xPixelLeftInt = (int)(xPixelLeft);
		int xPixelRightInt = (int)(xPixelRight);
		
		if(xPixelLeftInt < 0) {
			xPixelLeftInt = 0;
		}
		if(xPixelRightInt > width) {
			xPixelRightInt = width;
		}
		
		double yPixelLeftTop = (int) (yCornerTL / rotLeftSideZ * height + height / 2.0);
		double yPixelLeftBottom = yCornerBL / rotLeftSideZ * height + height / 2.0;
		double yPixelRightTop = yCornerTR / rotRightSideZ * height + height / 2.0;
		double yPixelRightBottom = yCornerBR / rotRightSideZ * height + height / 2.0;
		
		double tex1 = 1 / rotLeftSideZ;
		double tex2 = 1 /  rotRightSideZ;
		double tex3 = 0 / rotLeftSideZ;
		double tex4 = 8 / rotRightSideZ - tex3;
		
		for (int x = xPixelLeftInt; x < xPixelRightInt; x++) {
			double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);
			
			int xTexture = (int)((tex3 + tex4 * pixelRotation) / (tex1 + (tex2 - tex1) * pixelRotation));
			
			double yPixelTop = yPixelLeftTop + (yPixelRightTop - yPixelLeftTop) * pixelRotation;
			double yPixelBottom = yPixelLeftBottom + (yPixelRightBottom - yPixelLeftBottom) * pixelRotation;
			
			int yPixelTopInt = (int) (yPixelTop);
			int yPixelBottomInt = (int) (yPixelBottom);
			
			if (yPixelTopInt < 0) {
				yPixelTopInt = 0;
			}
			if (yPixelBottomInt > height) {
				yPixelBottomInt = height;
			}
			
			for (int y = yPixelTopInt; y < yPixelBottomInt; y++) {
				double pixelRotationY = (y - yPixelTop) / (yPixelBottom - yPixelTop);
				int yTexture = (int) (8 * pixelRotationY);
				pixels[x + y * width] = xTexture * 100 + yTexture * 100 * 256;					
				zBuffer[x + y * width] = 1 / (tex1 + (tex2 - tex1) * pixelRotation) * 8;
			}
			
		}
		
	}
	
	public void renderDistanceLimiter() {
		for (int i = 0; i < width * height; i++) {
			int colour = pixels[i];
			int brightness = (int) (renderDistance / (zBuffer[i]));
			
			if (brightness < 0) {
				brightness = 0;
			}
			
			if (brightness > 255) {
				brightness = 255;
			}
			
			int r = (colour >> 16) & 0xff;
			int g = (colour >> 8) & 0xff;
			int b = (colour) & 0xff;
			
			r = r * brightness / 255;
			g = g * brightness / 255;
			b = b * brightness / 255;
			
			pixels[i] = r << 16 | g << 8 | b;
		}
	}
	
}


