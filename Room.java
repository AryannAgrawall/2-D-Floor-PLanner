import java.awt.*;

public class Room {
    private String name;
    private int x, y, width, height;
    private Color color;

    private static final int GRID_SIZE = 20;  // Grid size is 20 pixels

    // Constructor that assigns colors based on room type
    public Room(String name, int width, int height) {
        this.name = name;
        this.width = (int) (Math.round((double) width / GRID_SIZE) * GRID_SIZE);
        this.height = (int) (Math.round((double) height / GRID_SIZE) * GRID_SIZE);

        // Snap the initial position to the nearest grid point
        this.x = (int) (Math.round(100.0 / GRID_SIZE) * GRID_SIZE);
        this.y = (int) (Math.round(100.0 / GRID_SIZE) * GRID_SIZE);

        // Assign colors based on room type
        switch (name) {
            case "Bedroom":
                this.color = Color.BLUE;
                break;
            case "Kitchen":
                this.color = Color.RED;
                break;
            case "Living Room":
                this.color = Color.GREEN;
                break;
            case "Bathroom":
                this.color = Color.CYAN;
                break;
            default:
                this.color = Color.GRAY;  // Default color for unknown room types
        }
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // Check if a point is within the room
    public boolean contains(Point p) {
        return p.x >= x && p.x <= x + width && p.y >= y && p.y <= y + height;
    }

    // Check if a point is near any corner for resizing
    public boolean isNearCorner(Point p) {
        int cornerMargin = 10;  // Margin around the corners to trigger resizing
        return (Math.abs(p.x - (x + width)) <= cornerMargin && Math.abs(p.y - (y + height)) <= cornerMargin);
    }

    // Move the room by a specified delta
    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    // Resize the room (if near a corner)
    public void resize(int dx, int dy) {
        this.width += dx;
        this.height += dy;

        // Snap room size to the grid
        this.width = (int) (Math.round((double) width / GRID_SIZE) * GRID_SIZE);
        this.height = (int) (Math.round((double) height / GRID_SIZE) * GRID_SIZE);

        // Ensure the room is never smaller than the grid size
        if (this.width < GRID_SIZE) this.width = GRID_SIZE;
        if (this.height < GRID_SIZE) this.height = GRID_SIZE;
    }

    // Snap the room's position to the nearest grid point
    public void snapToGrid() {
        this.x = (int) (Math.round((double) x / GRID_SIZE) * GRID_SIZE);
        this.y = (int) (Math.round((double) y / GRID_SIZE) * GRID_SIZE);
    }

    // Draw the room on the canvas
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);  // Draw the room
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);  // Draw the border
        g.drawString(name, x + 5, y + 15);  // Label the room
    }

    // Check if this room overlaps with another room
    public boolean overlaps(Room other) {
        return this.x < other.x + other.width &&
               this.x + this.width > other.x &&
               this.y < other.y + other.height &&
               this.y + this.height > other.y;
    }

    // Adjust room size to snap to the grid
    public void adjustSizeToGrid() {
        this.width = (int) (Math.round((double) width / GRID_SIZE) * GRID_SIZE);
        this.height = (int) (Math.round((double) height / GRID_SIZE) * GRID_SIZE);
    }
}
