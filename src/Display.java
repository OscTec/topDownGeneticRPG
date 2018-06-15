import processing.core.PApplet;
import processing.core.PVector;

import java.io.Serializable;

public class Display implements Serializable {

    void drawHero(PApplet p, PVector position, float theta, float r) {
        p.pushMatrix();
        p.fill(127);
        p.stroke(0);
        p.strokeWeight(1);
        p.translate(position.x, position.y);
        p.rotate(theta);
        p.beginShape();
        p.vertex(0, -r * 2);
        p.vertex(-r, r * 2);
        p.vertex(r, r * 2);
        p.endShape(p.CLOSE);
        p.popMatrix();
    }

    void drawProjectile(PApplet p, PVector position, float theta, int currentHealth, int maxHealth) {
        //p.noFill();
        p.fill(50,205,50);
        //p.strokeWeight(5);
        float m = p.map(currentHealth, 0, maxHealth, 0, 2);
        p.arc(position.x, position.y, 30, 30, 0, m*(p.PI), p.PIE);
        p.pushMatrix();
        p.fill(220,20,60);
        p.ellipse(position.x, position.y, 15, 15);
//        p.rectMode(p.RADIUS);
//        p.translate(position.x, position.y);
//        p.rotate(theta);
//        p.rect(0, 0, 0, 10);
        p.popMatrix();


    }
}
