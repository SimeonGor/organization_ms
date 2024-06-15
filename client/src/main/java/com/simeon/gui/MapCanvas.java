package com.simeon.gui;

import com.simeon.element.Coordinates;
import com.simeon.element.Organization;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

public class MapCanvas extends JPanel {
    private final ReentrantLock lock = new ReentrantLock();

    public class Animation extends TimerTask {
        private final Entity entity;
        public Animation(Entity entity) {
            this.entity = entity;
        }
        @Override
        public void run() {
            if (entity.percent < 1) {
                entity.percent += 0.1;
                repaint();
                return;
            }
            this.cancel();
        }
    }

    public static class Entity extends JPanel {
        private final Coordinates coordinates;
        private double percent;
        @Setter
        private boolean isSelected = false;

        public Entity(Coordinates coordinates) {
            this.coordinates = coordinates;
            percent = 0;
            setOpaque(false);
        }

        @Override
        public int hashCode() {
            return coordinates.hashCode();
        }
        @Override
        public boolean equals(Object other) {
            if (other == this) return true;
            if (other == null || other.getClass() != getClass()) return false;
            Entity e = (Entity) other;
            return e.coordinates.getY() == coordinates.getY() && e.coordinates.getX() == coordinates.getX();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            Point point = new Point(coordinates.getX(), (int) coordinates.getY());
            if (isSelected) {
                g2.setColor(Color.RED);
            }
            else {
                g2.setColor(Color.BLUE);
            }

            g2.fillArc(point.x - r, point.y - r, r * 2, r * 2, 90, (int) (-percent * 360));

            g2.dispose();
        }
    }

    private final HashMap<Long, Entity> collection = new HashMap<>();
    private int scale;
    private static final int r = 10;
    private static final int  margin = 20;

    private Point zero;
    private long selectedId = 0;

    public MapCanvas(GUI gui) {

        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        this.setPreferredSize(new Dimension(1000, 500));


        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point point = e.getPoint();
                int x = point.x - zero.x;
                int y = point.y - zero.y;

                for (var it : collection.entrySet()) {
                    Coordinates coord = it.getValue().coordinates;
                    if (Math.pow(x - coord.getX(), 2) + Math.pow(y - coord.getY(), 2) <= Math.pow(r, 2)) {
                        if (e.getClickCount() == 2) {
                            gui.update(it.getKey());
                        }
                        else {
                            gui.select(it.getKey());
                        }
                        return;
                    }
                }
            }
        });
    }

    private Point drawCoordinateSystem(Graphics g) {
        int cnt = 20;
        if (!collection.isEmpty()) {
            int minX = collection.values().stream()
                    .min((Comparator.comparingInt((o) -> o.coordinates.getX()))).get().coordinates.getX();
            int minY = (int) collection.values().stream()
                    .min((Comparator.comparingLong((o) -> o.coordinates.getY()))).get().coordinates.getY();

//        setSize(new Dimension(maxX - minX + margin * 2, (int) (maxY - minY + margin * 2)));

            Dimension size = getSize();
            int zeroX = margin + Math.abs(Math.min(0, minX));
            int zeroY = margin + Math.abs(Math.min(0, minY));
            g.drawLine(zeroX, 0, zeroX, size.height);
            g.drawLine(0, zeroY, size.width, zeroY);

            scale = Math.max(size.width, size.height) / cnt;

            for (int x = zeroX + scale; x < size.width; x += scale) {
                g.drawLine(x, zeroY - 5, x, zeroY + 5);
                String i = String.valueOf(x - zeroX);
                g.drawChars(i.toCharArray(), 0, i.length(), x, zeroY + 10);
            }
            for (int x = zeroX - scale; x > 0; x -= scale) {
                g.drawLine(x, zeroY - 5, x, zeroY + 5);
                String i = String.valueOf(x - zeroX);
                g.drawChars(i.toCharArray(), 0, i.length(), x, zeroY + 10);
            }

            for (int y = zeroY + scale; y < size.height; y += scale) {
                g.drawLine(zeroX - 5, y, zeroX + 5, y);
                String i = String.valueOf(y - zeroY);
                g.drawChars(i.toCharArray(), 0, i.length(), zeroX + 10, y);
            }
            for (int y = zeroY - scale; y > 0; y -= scale) {
                g.drawLine(zeroX - 5, y, zeroX + 5, y);
                String i = String.valueOf(y - zeroY);
                g.drawChars(i.toCharArray(), 0, i.length(), zeroX + 10, y);
            }


            return new Point(zeroX, zeroY);
        }

        return new Point(0, 0);
    }

    @Override
    public void paintComponent(Graphics g) {
        lock.lock();
        try {
            super.paintComponent(g);
            this.zero = drawCoordinateSystem(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.translate(zero.x, zero.y);
            for (var it : collection.entrySet()) {
                Entity entity = it.getValue();
                entity.paintComponent(g2);
            }
            g2.dispose();
        }
        finally {
            lock.unlock();
        }
    }

//    @Override
//    public void update(Graphics g) {
//        g.clearRect(0, 0, getWidth(), getHeight());
//        paint(g);
//    }

    public void add(Organization organization) {
        lock.lock();
        try {
            Entity entity = new Entity(organization.getCoordinates());
            long id = organization.getId();
            if (collection.containsKey(id) && collection.get(id).equals(entity)) return;
            collection.put(id, entity);
            Timer timer = new Timer();
            timer.schedule(new Animation(entity), 0, 100);
        }
        finally {
            lock.unlock();
        }
    }

    public void select(Organization organization) {
        lock.lock();
        try {
            if (collection.containsKey(selectedId)) {
                collection.get(selectedId).setSelected(false);
            }
            selectedId = organization.getId();
            collection.get(selectedId).setSelected(true);

            repaint();
        }
        finally {
            lock.unlock();
        }
    }

    public void delete(Organization organization) {
        lock.lock();
        try {
            collection.remove(organization.getId());
        }
        finally {
            lock.unlock();
        }
    }
}
