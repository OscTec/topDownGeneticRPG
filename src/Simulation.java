import processing.core.PApplet;
import processing.core.PVector;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Simulation {
    PApplet p;
    private Display d = new Display();
    ArrayList<Minion> leftMinions = new ArrayList<>();
    ArrayList<Tower> leftTowers = new ArrayList<>();
    Headquarters leftHQ;

    ArrayList<Minion> rightMinions = new ArrayList<>();
    ArrayList<Tower> rightTowers = new ArrayList<>();
    Headquarters rightHQ;

    ArrayList<PVector> topLane;
    ArrayList<PVector> midLane;
    ArrayList<PVector> btmLane;

    ArrayList<Projectile> leftProjectiles = new ArrayList<>();
    ArrayList<Projectile> rightProjectiles = new ArrayList<>();

    private boolean makeMinions = true;
    private boolean leftMinionsDead = false;
    private boolean rightMinionsDead = false;
    private boolean finished = false;


    private Minion bestMinion;
    private ArrayList<Minion> finishedMinions = new ArrayList<>();

    Simulation(PApplet p, ArrayList<Tower> leftTowers, ArrayList<Tower> rightTowers, Headquarters leftHQ, Headquarters rightHQ, ArrayList<PVector> topLane, ArrayList<PVector> midLane, ArrayList<PVector> btmLane) {
        this.p = p;
        for (Tower t : leftTowers) {
            this.leftTowers.add(copyTower(p, t.getPos(), t.getCurrentHealth(), t.getPlayer()));
        }
        for (Tower t : rightTowers) {
            this.rightTowers.add(copyTower(p, t.getPos(), t.getCurrentHealth(), t.getPlayer()));
        }
        //this.leftTowers = leftTower;
        this.leftHQ = leftHQ;

        //this.rightTowers = rightTower;
        this.rightHQ = rightHQ;

        this.topLane = topLane;
        this.midLane = midLane;
        this.btmLane = btmLane;
        buildMinions();
    }

    Simulation(PApplet p, ArrayList<Tower> leftTowers, ArrayList<Tower> rightTowers, Headquarters leftHQ, Headquarters rightHQ, ArrayList<PVector> topLane, ArrayList<PVector> midLane, ArrayList<PVector> btmLane, Minion m1, Minion m2, Minion m3) {
        this.p = p;
        for (Tower t : leftTowers) {
            this.leftTowers.add(copyTower(p, t.getPos(), t.getCurrentHealth(), t.getPlayer()));
        }
        for (Tower t : rightTowers) {
            this.rightTowers.add(copyTower(p, t.getPos(), t.getCurrentHealth(), t.getPlayer()));
        }
        //this.leftTowers = leftTower;
        this.leftHQ = leftHQ;

        //this.rightTowers = rightTower;
        this.rightHQ = rightHQ;

        this.topLane = topLane;
        this.midLane = midLane;
        this.btmLane = btmLane;
        makeMinions = false;
        buildMinions(m1, m2, m3);

    }

    void tick() {
        //d.drawLanes(p);
        for (Projectile pro : leftProjectiles) {
            if (!pro.projectileAlive()) {
                leftProjectiles.remove(pro);
                return;
            } else {
                //pro.drawProjectile(p);//To be removed after testing
                pro.tick(p);
            }
        }
        for (Projectile pro : rightProjectiles) {
            if (!pro.projectileAlive()) {
                rightProjectiles.remove(pro);
                return;
            } else {
//                pro.drawProjectile(p);//To be removed after testing
                pro.tick(p);
            }
        }

        for (Tower t : leftTowers) {
            if (t.checkDead()) {
                leftTowers.remove(t);
                return;
            }
            t.tick();
        }
        for (Tower t : rightTowers) {
            if (t.checkDead()) {
                rightTowers.remove(t);
                return;
            }
            t.tick();
        }
        if (leftMinions.isEmpty()) {
            leftMinionsDead = true;
        }
        if (rightMinions.isEmpty()) {
            rightMinionsDead = true;
        }

        for (Minion m : leftMinions) {

            if (m.checkDead()) {
                leftMinions.remove(m);
                return;
            }
            //m.drawMinion();
            m.tick(p);
        }
        for (Minion m : rightMinions) {

            if (m.checkDead()) {
                finishedMinions.add(copyMinion(p, m.getHltPoints(), m.getSpdPoints(), m.getRngPoints(), m.getDmgPoints(), m.getAtsPoints(), m.getDamageDealt()));
                //bestMinion = copyMinion(p, m.getHealth(), m.getSpeed(), m.getRange(), m.getDamage(), m.getAtkSpeed(), m.getDamageDealt());
                //System.out.println("Right minion dead");
//                if (ai.testMinion(m)) {
//                    int health = (int) deepClone(m.getHealth());
//                    int speed = (int) deepClone(m.getSpeed());
//                    int range = (int) deepClone(m.getRange());
//                    int damage = (int) deepClone(m.getDamage());
//                    float atkSpeed = (float) deepClone(m.getAtkSpeed());
//                    ai.saveMinion(p, health, speed, range, damage, atkSpeed);
//                    ai.modStats();
//                }
                rightMinions.remove(m);
                return;
            }
            //m.drawMinion();
            //System.out.println(m.getPos());
            m.tick(p);
            //System.out.println(rightMinions);
        }
        if (leftMinionsDead && rightMinionsDead) {
            //Environment.unpause();
            leftMinionsDead = true;
            rightMinionsDead = true;
            //System.out.println("Minions dead");
        }

    }

    void buildMinions() {
        rightMinions.add(new Minion(p, new PVector(p.width - 130, p.height / 2f), topLane, this, false));
        rightMinions.add(new Minion(p, new PVector(p.width - 130, p.height / 2f), midLane, this, false));
        rightMinions.add(new Minion(p, new PVector(p.width - 130, p.height / 2f), btmLane, this, false));

        leftMinions.add(new Minion(p, new PVector(100, p.height / 2f), topLane, this, true));
        leftMinions.add(new Minion(p, new PVector(100, p.height / 2f), midLane, this, true));
        leftMinions.add(new Minion(p, new PVector(100, p.height / 2f), btmLane, this, true));
    }

    void buildMinions(Minion m1, Minion m2, Minion m3) {
        rightMinions.add(new Minion(p, new PVector(p.width - 130, p.height / 2f), topLane, this, false, m1.getHltPoints(), m1.getSpdPoints(), m1.getAtsPoints(), m1.getRngPoints(), m1.getDmgPoints()));
        rightMinions.add(new Minion(p, new PVector(p.width - 130, p.height / 2f), midLane, this, false, m2.getHltPoints(), m2.getSpdPoints(), m2.getAtsPoints(), m2.getRngPoints(), m2.getDmgPoints()));
        rightMinions.add(new Minion(p, new PVector(p.width - 130, p.height / 2f), btmLane, this, false, m3.getHltPoints(), m3.getSpdPoints(), m3.getAtsPoints(), m3.getRngPoints(), m3.getDmgPoints()));

        leftMinions.add(new Minion(p, new PVector(100, p.height / 2f), topLane, this, true));
        leftMinions.add(new Minion(p, new PVector(100, p.height / 2f), midLane, this, true));
        leftMinions.add(new Minion(p, new PVector(100, p.height / 2f), btmLane, this, true));
    }

    boolean simFinished() {
        if (rightMinionsDead) {
            return true;
        } else {
            return false;
        }
//        if (leftMinionsDead && rightMinionsDead) {
//            return true;
//        } else {
//            return false;
//        }

    }
    /*
    void addRightProjectile(PVector position, PVector velocity, int damage, int range) {
        PVector bPos = (PVector) deepClone(position);
        PVector bVel = (PVector) deepClone(velocity);
        rightProjectiles.add(new Projectile(bPos, bVel, damage, range));
    }

    void addLeftProjectile(PVector position, PVector velocity, int damage, int range) {
        PVector bPos = (PVector) deepClone(position);
        PVector bVel = (PVector) deepClone(velocity);
        leftProjectiles.add(new Projectile(bPos, bVel, damage, range));
    }
    */

    void addRightProjectile(PVector position, PVector velocity, int damage) {
        PVector bPos = (PVector) deepClone(position);
        PVector bVel = (PVector) deepClone(velocity);
        rightProjectiles.add(new Projectile(bPos, bVel, damage));
    }

    void addLeftProjectile(PVector position, PVector velocity, int damage) {
        PVector bPos = (PVector) deepClone(position);
        PVector bVel = (PVector) deepClone(velocity);
        leftProjectiles.add(new Projectile(bPos, bVel, damage));
    }


    Minion getBestMinion() {
        return bestMinion;
    }

    private Object deepClone(Object object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    ArrayList<Minion> getFinihsedMinions() {
        return finishedMinions;
    }

    Tower copyTower(PApplet p, PVector pos, int currentHealth, boolean player) {
        PVector bPos = (PVector) deepClone(pos);
        int bHealth = (int) deepClone(currentHealth);
        boolean bPlayer = (boolean) deepClone(player);
        return new Tower(p, this, bPos, bPlayer, bHealth);
    }

    Minion copyMinion(PApplet p, int hltPoints, int spdPoints, int rngPoints, int dmgPoints, int atsPoints, int damageDealt) {
        int bHltPoints = (int) deepClone(hltPoints);
        int bSpdPoints = (int) deepClone(spdPoints);
        int bRngPoints = (int) deepClone(rngPoints);
        int bDmgPoints = (int) deepClone(dmgPoints);
        int bAtsPoints = (int) deepClone(atsPoints);
        int bDamageDealt = (int) deepClone(damageDealt);

        return new Minion(p, bHltPoints, bSpdPoints, bRngPoints, bDmgPoints, bAtsPoints, bDamageDealt);
    }
}
