package it.cs.unicam.pa2021.logo.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class InstructionTest {

    @Test
    public void cursorDirectionShouldBeChanged() {
        Plane<Point<Double>> plane = new DefaultPlane(500, 500);
        assertEquals(plane.getHome(), plane.getCursorPosition());
        Instruction<Point<Double>> i = Instruction::left;
        plane = i.execute(plane, 90);
        assertEquals(90, plane.getCursor().getDirection().getDirectionWay());
        plane = i.execute(plane, 360);
        assertEquals(90, plane.getCursor().getDirection().getDirectionWay());
        i = Instruction::right;
        plane = i.execute(plane, 90);
        assertEquals(0, plane.getCursor().getDirection().getDirectionWay());
        plane = i.execute(plane, 360);
        assertEquals(0, plane.getCursor().getDirection().getDirectionWay());
        plane = i.execute(plane, 42);
        assertEquals(318, plane.getCursor().getDirection().getDirectionWay());
    }


    @Test
    public void cursorShouldForwardInPlane() {
        Plane<Point<Double>> plane = new DefaultPlane(50, 100);
        assertEquals(plane.getHome(), plane.getCursorPosition());
        Instruction<Point<Double>> i = Instruction::left;
        plane = i.execute(plane, 180);
        assertEquals(Directional.simpleDirection(180).getDirectionWay(), plane.getCursor().getDirection().getDirectionWay());
        i = Instruction::forward;
        plane = i.execute(plane, 20);
        assertEquals(Point.cartesianPoint(5.0, 50.0), plane.getCursor().getPosition());
        Point<Double> A = plane.getHome();
        Point<Double> B = Point.cartesianPoint(5, 50);
        Line<Point<Double>> l0 = new Segment<>(A, B, plane.getCursor().getLineColor(), plane.getCursor().getPenSize());
        assertTrue(plane.getLinesAt(Point.cartesianPoint(25, 50)).contains(l0));
        assertTrue(plane.getLines().contains(l0));
        i = Instruction::right;
        plane = i.execute(plane, 270);
        assertEquals(Directional.simpleDirection(270).getDirectionWay(), plane.getCursor().getDirection().getDirectionWay());
        i = Instruction::forward;
        plane = i.execute(plane, 50);
        assertEquals(Point.cartesianPoint(5.0, 0.0), plane.getCursorPosition());
        i = Instruction::backward;
        plane = i.execute(plane, 80);
        assertEquals(Point.cartesianPoint(5.0, 80.0), plane.getCursorPosition());
        i = Instruction::forward;
        plane = i.execute(plane, 40);
        assertEquals(Point.cartesianPoint(5.0, 40.0), plane.getCursorPosition());
    }

    @Test
    public void cursorShouldBackwardInPlane() {
        Plane<Point<Double>> plane = new DefaultPlane(200, 400);
        assertEquals(plane.getHome(), plane.getCursorPosition());
        Instruction<Point<Double>> i = Instruction::right;
        plane = i.execute(plane, 45);
        assertFalse(plane.getCursor().isPlot());
        assertEquals(Directional.simpleDirection(315).getDirectionWay(), plane.getCursor().getDirection().getDirectionWay());
        i = Instruction::backward;
        plane = i.execute(plane, 37);
        assertTrue(plane.getCursor().isPlot());
        assertEquals(Point.cartesianPoint(73.84, 226.16), plane.getCursor().getPosition());
        Point<Double> A = plane.getHome();
        Point<Double> B = Point.cartesianPoint(73.84, 226.16);
        Line<Point<Double>> l0 = new Segment<>(A, B, plane.getCursor().getLineColor(), plane.getCursor().getPenSize());
        assertTrue(plane.getLinesAt(plane.getHome()).contains(l0));
        assertTrue(plane.getLines().contains(l0));
        i = Instruction::right;
        plane = i.execute(plane, 270);
        assertEquals(Directional.simpleDirection(45).getDirectionWay(), plane.getCursor().getDirection().getDirectionWay());
        i = Instruction::forward;
        plane = i.execute(plane, 21);
        assertEquals(Point.cartesianPoint(88.69, 241.01), plane.getCursorPosition());
        i = Instruction::backward;
        plane = i.execute(plane, 21);
        assertEquals(Point.cartesianPoint(73.84, 226.16), plane.getCursorPosition());
        i = Instruction::forward;
        plane = i.execute(plane, 40);
        plane = i.execute(plane, 0);
        assertEquals(Point.cartesianPoint(102.12, 254.44), plane.getCursorPosition());
        assertEquals(4, plane.getNumLines());
        assertEquals(4, plane.getNumPoints());
        assertEquals(0, plane.getNumClosedAreas());
    }

    @Test
    public void screenShouldBeCleaned() {
        Plane<Point<Double>> plane = new DefaultPlane(200, 400);
        plane.addLine(new Segment<>(Point.cartesianPoint(10, 20), Point.cartesianPoint(20, 30), plane.getCursor().getLineColor(),plane.getCursor().getPenSize()));
        plane.addLine(new Segment<>(Point.cartesianPoint(20, 30), Point.cartesianPoint(40, 20), plane.getCursor().getLineColor(),plane.getCursor().getPenSize()));
        plane.addLine(new Segment<>(Point.cartesianPoint(40, 20), Point.cartesianPoint(120, 17), plane.getCursor().getLineColor(),plane.getCursor().getPenSize()));
        plane.addLine(new Segment<>(Point.cartesianPoint(120, 17), Point.cartesianPoint(10, 20), plane.getCursor().getLineColor(),plane.getCursor().getPenSize()));
        assertEquals(4, plane.getNumLines());
        assertEquals(1, plane.getNumClosedAreas());
        assertEquals(4, plane.getNumPoints());
        Instruction<Point<Double>> i = Instruction::clearScreen;
        plane = i.execute(plane);
        assertEquals(0, plane.getNumLines());
        assertEquals(0, plane.getNumClosedAreas());
        assertEquals(0, plane.getNumPoints());
    }

    @Test
    public void cursorShouldBeMovedToHomeInPlane() {
        Plane<Point<Double>> plane = new DefaultPlane(160, 200);
        assertEquals(plane.getHome(), plane.getCursorPosition());
        Instruction<Point<Double>> i = Instruction::right;
        plane = i.execute(plane, 270);
        assertEquals(90, plane.getCursor().getDirection().getDirectionWay());
        i = Instruction::backward;
        plane = i.execute(plane, 50);
        assertEquals(Point.cartesianPoint(80, 50), plane.getCursorPosition());
        i = Instruction::home;
        plane = i.execute(plane);
        assertEquals(plane.getHome(), plane.getCursorPosition());
        assertEquals(1, plane.getNumLines());
        assertEquals(2, plane.getNumPoints());
    }

    @Test
    public void penShouldBeUpAndDown() {
        Plane<Point<Double>> plane = new DefaultPlane(300, 300);
        Instruction<Point<Double>> i = Instruction::penUp;
        plane = i.execute(plane);
        assertFalse(plane.getCursor().isPen());
        i = Instruction::forward;
        plane = i.execute(plane, 50);
        assertFalse(plane.getCursor().isPlot());
        assertEquals(Point.cartesianPoint(200, 150), plane.getCursorPosition());
        assertEquals(0, plane.getNumLines());
        assertEquals(0, plane.getNumPoints());
        i = Instruction::penDown;
        plane = i.execute(plane);
        assertTrue(plane.getCursor().isPen());
        i = Instruction::backward;
        plane = i.execute(plane, 28);
        assertTrue(plane.getCursor().isPlot());
        assertEquals(1, plane.getNumLines());
        assertEquals(2, plane.getNumPoints());
        assertTrue(plane.getLinesAt(plane.getCursorPosition()).contains(new Segment<>(Point.cartesianPoint(200, 150), plane.getCursorPosition(), plane.getCursor().getLineColor(),plane.getCursor().getPenSize())));
    }

    @Test
    public void penColorShouldBeChanged() {
        Plane<Point<Double>> plane = new DefaultPlane(400, 800);
        assertEquals(plane.getCursor().getLineColor(), new RGBColor(0, 0, 0));
        Instruction<Point<Double>> i = Instruction::setPenColor;
        plane = i.execute(plane, 255, 0, 0);    // RED
        assertEquals(new RGBColor(255, 0, 0), plane.getCursor().getLineColor());
        plane = i.execute(plane, 0, 255, 0);    // GREEN
        assertEquals(new RGBColor(0, 255, 0), plane.getCursor().getLineColor());
        plane = i.execute(plane, 0, 0, 255);    // BLUE
        assertEquals(new RGBColor(0, 0, 255), plane.getCursor().getLineColor());
    }

    @Test
    public void fillColorShouldBeChanged() {
        Plane<Point<Double>> plane = new DefaultPlane(400, 800);
        assertEquals(plane.getCursor().getAreaColor(), new RGBColor(255, 255, 255));
        Instruction<Point<Double>> i = Instruction::setFillColor;
        plane = i.execute(plane, 0, 255, 0);    // MAGENTA
        i = Instruction::right;
        plane = i.execute(plane, 90);
        i = Instruction::forward;
        plane = i.execute(plane, 50);
        i = Instruction::repeat;
        plane = i.execute(plane, 2, Arrays.asList("RIGHT", "120", "FORWARD", "50"));
        assertEquals(3, plane.getNumLines());
        assertEquals(Objects.requireNonNull(plane.getClosedAreas().peek()).getColor(), new RGBColor(0, 255, 0));
    }

    @Test
    public void screenColorShouldBeChanged() {
        Plane<Point<Double>> plane = new DefaultPlane(300, 250);
        assertEquals(plane.getBackgroundColor(), new RGBColor(255, 255, 255));
        Instruction<Point<Double>> i = Instruction::setScreenColor;
        plane = i.execute(plane, 255, 255, 0);    // YELLOW
        assertEquals(new RGBColor(255, 255, 0), plane.getBackgroundColor());
    }

    @Test
    public void penSizeShouldBeChanged() {
        Plane<Point<Double>> plane = new DefaultPlane(300, 250);
        assertEquals(1, plane.getCursor().getPenSize());
        Instruction<Point<Double>> i = Instruction::setPenSize;
        plane = i.execute(plane, 7);
        assertEquals(7, plane.getCursor().getPenSize());
        Plane<Point<Double>> finalPlane = plane;
        assertThrows(IllegalArgumentException.class, () -> i.execute(finalPlane, 0));
    }

    @Test
    public void RedSquareShouldBeCreated() {
        Plane<Point<Double>> plane = new DefaultPlane(1000, 1000);
        Instruction<Point<Double>> i = Instruction::setFillColor;
        plane = i.execute(plane, 255, 0, 0);
        i = Instruction::repeat;
        plane = i.execute(plane, 4, Arrays.asList("FORWARD", "50", "RIGHT", "90"));
        Line<Point<Double>> a, b, c, d;
        a = new Segment<>(plane.getHome(), Point.cartesianPoint(550, 500), plane.getCursor().getLineColor(),plane.getCursor().getPenSize());
        b = new Segment<>(Point.cartesianPoint(550, 500), Point.cartesianPoint(550, 450), plane.getCursor().getLineColor(),plane.getCursor().getPenSize());
        c = new Segment<>(Point.cartesianPoint(550, 450), Point.cartesianPoint(500, 450), plane.getCursor().getLineColor(),plane.getCursor().getPenSize());
        d = new Segment<>(Point.cartesianPoint(500, 450), plane.getHome(), plane.getCursor().getLineColor(),plane.getCursor().getPenSize());
        ClosedArea<Line<Point<Double>>> expectedSquare = new SimpleArea(Arrays.asList(a, b, c, d),
                new RGBColor(255, 10, 0));
        assertEquals(4, plane.getNumLines());
        assertEquals(4, plane.getNumPoints());
        assertEquals(1, plane.getNumClosedAreas());
        assertNotEquals(expectedSquare, plane.getClosedAreas().peek());
        expectedSquare = new SimpleArea(Arrays.asList(a, b, c, d), new RGBColor(255, 0, 0));
        assertEquals(expectedSquare, plane.getClosedAreas().peek());
    }

    @Test
    public void IllegalArgumentExceptionShouldBeThrown() {
        Plane<Point<Double>> plane = new DefaultPlane(10, 10);
        movementException(plane, Instruction::forward);
        movementException(plane, Instruction::backward);
        directionException(plane, Instruction::left);
        directionException(plane, Instruction::right);
        setColorException(plane, Instruction::setPenColor);
        setColorException(plane, Instruction::setFillColor);
        setColorException(plane, Instruction::setScreenColor);
        Instruction<Point<Double>> sps = Instruction::setPenSize;
        assertThrows(IllegalArgumentException.class, () -> sps.execute(plane, 0));
    }

    private void movementException(Plane<Point<Double>> plane, Instruction<Point<Double>> instr) {
        assertThrows(IllegalArgumentException.class, () -> instr.execute(plane, -1));
    }

    private void directionException(Plane<Point<Double>> plane, Instruction<Point<Double>> instr) {
        assertThrows(IllegalArgumentException.class, () -> instr.execute(plane, -1));
        assertThrows(IllegalArgumentException.class, () -> instr.execute(plane, 361));
    }

    private void setColorException(Plane<Point<Double>> plane, Instruction<Point<Double>> instr) {
        assertThrows(IllegalArgumentException.class, () -> instr.execute(plane, new RGBColor(-1, 2, 3)));
        assertThrows(IllegalArgumentException.class, () -> instr.execute(plane, new RGBColor(1, -2, 3)));
        assertThrows(IllegalArgumentException.class, () -> instr.execute(plane, new RGBColor(1, 2, -3)));
        assertThrows(IllegalArgumentException.class, () -> instr.execute(plane, new RGBColor(256, 2, 3)));
        assertThrows(IllegalArgumentException.class, () -> instr.execute(plane, new RGBColor(2, 257, 3)));
        assertThrows(IllegalArgumentException.class, () -> instr.execute(plane, new RGBColor(56, 2, 301)));
    }

}