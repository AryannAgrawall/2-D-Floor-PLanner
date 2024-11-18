//mera kaam maine kardiya hai ab saara kaam tumhara
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class RoomCanvas extends JPanel {
    private final ArrayList<Room> rooms = new ArrayList<>();
    private Room selectedRoom = null;
    private Point dragStartPoint;
    private boolean isResizing = false;
    private boolean isMoving = false;

    public RoomCanvas() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(600, 600));
        setFocusable(true);

        // Mouse event listener for selecting, dragging, and resizing rooms
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                for (Room room : rooms) {
                    if (room.contains(e.getPoint())) {
                        selectedRoom = room;
                        dragStartPoint = e.getPoint();
                        if (selectedRoom.isNearCorner(e.getPoint())) {
                            isResizing = true;  // Start resizing if near a corner
                        } else {
                            isMoving = true;  // Start moving if not near a corner
                        }
                        break;
                    }
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (selectedRoom != null) {
                    if (isResizing) {
                        isResizing = false;
                        // Snap room size to grid
                        selectedRoom.adjustSizeToGrid();
                    } else if (isMoving) {
                        isMoving = false;
                        // Snap room position to grid
                        selectedRoom.snapToGrid(); // Adjust the room position
                    }
                }
                selectedRoom = null;
                repaint();
            }
        });

        // Mouse motion listener for dragging and resizing rooms
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (selectedRoom != null) {
                    int dx = e.getX() - dragStartPoint.x;
                    int dy = e.getY() - dragStartPoint.y;

                    if (isResizing) {
                        selectedRoom.resize(dx, dy);  // Resize the room
                    } else if (isMoving) {
                        selectedRoom.move(dx, dy);  // Move the room
                    }

                    dragStartPoint = e.getPoint();  // Update start point for next drag/resizing
                    repaint();  // Redraw the canvas
                }
            }
        });
    }

    // Add a new room to the canvas, snapping it to the grid
    public void addRoom(Room room) {
        room.adjustSizeToGrid();  // Adjust room size to fit the grid
        rooms.add(room);
        repaint();  // Redraw the canvas with the new room
    }

    // Delete the selected room from the canvas
    public void deleteSelectedRoom() {
        if (selectedRoom != null) {
            rooms.remove(selectedRoom);
            selectedRoom = null;  // Deselect room after deletion
            repaint();  // Redraw the canvas after deletion
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        for (Room room : rooms) {
            room.draw(g);  // Draw each room on the canvas
        }
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < getWidth(); i += 20) {
            g.drawLine(i, 0, i, getHeight());
        }
        for (int j = 0; j < getHeight(); j += 20) {
            g.drawLine(0, j, getWidth(), j);
        }
    }
}
