package it.cs.unicam.pa2021.logo.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class DefaultPlaneTest {
    @Test
    public void linesShouldBeGenerated() {
        Plane<Point<Double>> p = new DefaultPlane(10, 10);
        Instruction<Point<Double>> i = Instruction::penUp;
        p = i.execute(p);
        i = Instruction::repeat;
        p = i.execute(p, 1, Arrays.asList("BACKWARD", "5", "RIGHT", "90", "FORWARD", "1"));
        assertEquals(Point.cartesianPoint(0, 4), p.getCursorPosition());
        i = Instruction::penDown;
        p = i.execute(p);
        i = Instruction::left;
        p = i.execute(p, 90);
        i = Instruction::forward;
        p = i.execute(p, 6);
        i = Instruction::penUp;
        p = i.execute(p);
        assertEquals(Point.cartesianPoint(6, 4), p.getCursorPosition());
        i = Instruction::penDown;
        p = i.execute(p);
        i = Instruction::left;
        p = i.execute(p, 90);
        i = Instruction::forward;
        p = i.execute(p, 6);
        Line<Point<Double>> l0 = new Segment<>(Point.cartesianPoint(0, 4), Point.cartesianPoint(6, 4),p.getCursor().getLineColor(), p.getCursor().getPenSize());
        Line<Point<Double>> l1 = new Segment<>(Point.cartesianPoint(6, 4), Point.cartesianPoint(6, 9),p.getCursor().getLineColor(), p.getCursor().getPenSize());
        assertEquals(new HashSet<>(Arrays.asList(l0, l1)), p.getLinesAt(Point.cartesianPoint(6, 4)));
    }

    @Test
    public void oneClosedAreaShouldBeGeneratedTest() {
        Plane<Point<Double>> p = new DefaultPlane(500, 500);
        Line<Point<Double>> l0, l1, l2, l3;
        Instruction<Point<Double>> i = Instruction::left;
        p = i.execute(p, 90);
        assertEquals(new SimpleDirection(90).getDirectionWay(), p.getCursor().getDirection().getDirectionWay());
        i = Instruction::forward;
        p = i.execute(p, 50);
        assertEquals(Point.cartesianPoint(250, 300), p.getCursorPosition());
        l0 = new Segment<>(p.getHome(), p.getCursorPosition(),p.getCursor().getLineColor(), p.getCursor().getPenSize());
        assertTrue(p.getLines().contains(l0));
        i = Instruction::left;
        p = i.execute(p, 90);
        assertEquals(new SimpleDirection(180).getDirectionWay(), p.getCursor().getDirection().getDirectionWay());
        i = Instruction::forward;
        p = i.execute(p, 50);
        assertEquals(Point.cartesianPoint(200, 300), p.getCursorPosition());
        l1 = new Segment<>(p.getCursorPosition(), Point.cartesianPoint(250, 300),p.getCursor().getLineColor(), p.getCursor().getPenSize());
        assertTrue(p.getLines().contains(l1));
        i = Instruction::left;
        p = i.execute(p, 90);
        assertEquals(new SimpleDirection(270).getDirectionWay(), p.getCursor().getDirection().getDirectionWay());
        i = Instruction::forward;
        p = i.execute(p, 50);
        assertEquals(Point.cartesianPoint(200, 250), p.getCursorPosition());
        l2 = new Segment<>(p.getCursorPosition(), Point.cartesianPoint(200, 300),p.getCursor().getLineColor(), p.getCursor().getPenSize());
        assertTrue(p.getLines().contains(l2));
        i = Instruction::left;
        p = i.execute(p, 90);
        assertEquals(new SimpleDirection(0).getDirectionWay(), p.getCursor().getDirection().getDirectionWay());
        i = Instruction::forward;
        p = i.execute(p, 50);
        assertEquals(Point.cartesianPoint(250, 250), p.getCursorPosition());
        l3 = new Segment<>(p.getCursorPosition(), Point.cartesianPoint(200, 250),p.getCursor().getLineColor(), p.getCursor().getPenSize());
        assertTrue(p.getLines().contains(l3));
        assertEquals(4, p.getLines().size());
        assertEquals(1, p.getClosedAreas().size());
        assertEquals(Arrays.asList(l0, l1, l2, l3), Objects.requireNonNull(p.getClosedAreas().peek()).getArea());
    }

    @Test
    public void someClosedAreaShouldBeGenerated() {
        Plane<Point<Double>> plane = new DefaultPlane(1000, 1000);
        Instruction<Point<Double>> i = Instruction::repeat;
        plane = i.execute(plane, 4, Arrays.asList("FORWARD", "10", "RIGHT", "90"));
        assertEquals(4, plane.getNumPoints());
        assertEquals(4, plane.getNumLines());
        assertEquals(1, plane.getNumClosedAreas());
        plane = i.execute(plane, 1, Arrays.asList("LEFT", "90", "PENUP", "FORWARD", "100", "PENDOWN"));
        assertEquals(Point.cartesianPoint(500, 600), plane.getCursorPosition());
        plane = i.execute(plane, 4, Arrays.asList("FORWARD", "10", "RIGHT", "90"));
        assertEquals(Point.cartesianPoint(500, 600), plane.getCursorPosition());
        assertEquals(8, plane.getNumPoints());
        assertEquals(8, plane.getNumLines());
        assertEquals(2, plane.getNumClosedAreas());
        plane = i.execute(plane, 1, Arrays.asList("RIGHT", "90", "PENUP", "FORWARD", "200", "PENDOWN", "RIGHT", "90", "FORWARD", "50"));
        assertEquals(Point.cartesianPoint(700, 550), plane.getCursorPosition());
        plane = i.execute(plane, 2, Arrays.asList("RIGHT", "120", "FORWARD", "50"));
        assertEquals(11, plane.getNumPoints());
        assertEquals(11, plane.getNumLines());
        assertEquals(3, plane.getNumClosedAreas());
    }

    @Test
    public void noAreasShouldBeGenerated() {
        Plane<Point<Double>> plane = new DefaultPlane(1000, 1000);
        Instruction<Point<Double>> i = Instruction::forward;
        plane = i.execute(plane, 100);
        assertEquals(2, plane.getNumPoints());
        assertEquals(1, plane.getNumLines());
        assertEquals(0, plane.getNumClosedAreas());
    }

    @Test
    public void linesNotShouldBePartOfClosedArea(){
        Plane<Point<Double>> plane = new DefaultPlane(1000, 1000);
        Instruction<Point<Double>> i = Instruction::setFillColor;
        plane = i.execute(plane, 255, 0, 0);
        i = Instruction::repeat;
        plane = i.execute(plane, 8, Arrays.asList("FORWARD", "50", "RIGHT", "90"));
        assertEquals(8, plane.getNumLines());
        assertEquals(4, plane.getNumPoints());
        assertEquals(1, plane.getNumClosedAreas());
    }

    @Test
    public void IllegalArgumentExceptionShouldBeThrown() {
        assertThrows(IllegalArgumentException.class, () -> new DefaultPlane(-100, 200));
        assertThrows(IllegalArgumentException.class, () -> new DefaultPlane(100, -200));
        assertThrows(IllegalArgumentException.class, () -> new DefaultPlane(100, 200, Point.cartesianPoint(500, 500), Point.cartesianPoint(150, 201)));
    }

    @Test
    public void NullPointerExceptionShouldBeThrown() {
        assertThrows(NullPointerException.class, () -> new DefaultPlane(200, 200, null, Point.cartesianPoint(0, 0)));
        assertThrows(NullPointerException.class, () -> new DefaultPlane(200, 200, Point.cartesianPoint(10, 20), null));
        assertThrows(NullPointerException.class, () -> new DefaultPlane(200, 200, Point.cartesianPoint(10, 20), null));
        assertThrows(NullPointerException.class, () -> new DefaultPlane(300, 200, null, null));
    }

    @Test
    public void planeShouldBeCreated() {
        Plane<Point<Double>> plane = new DefaultPlane(400, 200);
        assertEquals(400, plane.getLength());
        assertEquals(200, plane.getHeight());
        assertEquals(Point.cartesianPoint(200, 100), plane.getHome());
        assertEquals(Point.cartesianPoint(0, 0), plane.getOrigin());
        assertEquals(new RGBColor(255, 255, 255), plane.getBackgroundColor());
        assertEquals(plane.getHome(), plane.getCursorPosition());
        plane = new DefaultPlane(300, 200, Point.cartesianPoint(50, 190), Point.cartesianPoint(150, 20));
        assertEquals(300, plane.getLength());
        assertEquals(200, plane.getHeight());
        assertEquals(Point.cartesianPoint(50, 190), plane.getHome());
        assertEquals(Point.cartesianPoint(150, 20), plane.getOrigin());
    }

    @Test
    public void pointsShouldBePartOfPlane() {
        Plane<Point<Double>> plane = new DefaultPlane(50, 70);
        assertTrue(plane.bePartOfPlane(Point.cartesianPoint(0, 0)));
        assertEquals(Point.cartesianPoint(0, 0), plane.getDownLeftPoint());
        assertTrue(plane.bePartOfPlane(Point.cartesianPoint(49, 69)));
        assertEquals(Point.cartesianPoint(49, 69), plane.getUpRightPoint());
        assertTrue(plane.bePartOfPlane(Point.cartesianPoint(0, 49)));
        assertEquals(Point.cartesianPoint(49, 0), plane.getDownRightPoint());
        assertTrue(plane.bePartOfPlane(Point.cartesianPoint(0, 69)));
        assertEquals(Point.cartesianPoint(0, 69), plane.getUpLeftPoint());
        assertFalse(plane.bePartOfPlane(Point.cartesianPoint(110, 30)));
        assertTrue(plane.bePartOfPlane(Point.cartesianPoint(49, 69)));
        assertFalse(plane.bePartOfPlane(Point.cartesianPoint(50, 70)));
    }

}