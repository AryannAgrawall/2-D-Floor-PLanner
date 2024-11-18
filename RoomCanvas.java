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
                            isResizing = true;
                        } else {
                            isMoving = true;
                        }
                        break;
                    }
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (selectedRoom != null) {
                    if (isResizing) {
                        isResizing = false;
                        selectedRoom.adjustSizeToGrid();
                        if (hasOverlap(selectedRoom)) {
                            JOptionPane.showMessageDialog(null, "Room overlaps with another room. Resize reverted.");
                            selectedRoom.resize(0, 0);  // Revert size change
                        }
                    } else if (isMoving) {
                        isMoving = false;
                        selectedRoom.snapToGrid();
                        if (hasOverlap(selectedRoom)) {
                            JOptionPane.showMessageDialog(null, "Room overlaps with another room. Move reverted.");
                            selectedRoom.move(0, 0);  // Revert move change
                        }
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
                        selectedRoom.resize(dx, dy);
                        if (hasOverlap(selectedRoom)) {
                            selectedRoom.resize(-dx, -dy);  // Revert if overlap occurs
                        }
                    } else if (isMoving) {
                        selectedRoom.move(dx, dy);
                        if (hasOverlap(selectedRoom)) {
                            selectedRoom.move(-dx, -dy);  // Revert if overlap occurs
                        }
                    }

                    dragStartPoint = e.getPoint();
                    repaint();
                }
            }
        });
    }

    // Add a new room, ensuring no overlap
    public void addRoom(Room room) {
        room.adjustSizeToGrid();
        if (!hasOverlap(room)) {
            rooms.add(room);
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Room overlaps with another room. Please try again.");
        }
    }

    // Delete the selected room
    public void deleteSelectedRoom() {
        if (selectedRoom != null) {
            rooms.remove(selectedRoom);
            selectedRoom = null;
            repaint();
        }
    }

    // Check for room overlap
    public boolean hasOverlap(Room roomToCheck) {
        for (Room room : rooms) {
            if (room != roomToCheck && room.overlaps(roomToCheck)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        for (Room room : rooms) {
            room.draw(g);
        }
    }

    // Draw the grid
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
