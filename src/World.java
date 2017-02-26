import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World
{
    List<RoadNode> intersections;
    List<Spawner> spawners;
    List<Car> cars;
    Random rand;

    public World()
    {
	intersections = new ArrayList<>();
	spawners = new ArrayList<>();
	cars = new ArrayList<>();
	rand = new Random();
    }
    
    public void update()
    {
	for(Spawner spawn : spawners)
	    if(rand.nextFloat() <= spawn.spawnWeight)
		cars.add(new Car(spawn.entrypoint, intersections.get(rand.nextInt(intersections.size()))));
	for(int i = 0; i < cars.size(); i++)
	{
	    if(cars.get(i).update())
	    {
		cars.remove(i);
		i--;
	    }
	}
    }

    public void populateRoads(int width, int height)
    {
	RoadNode node = new RoadNode(0, 0);
	populate(node, width, height);
	for (int i = 0; i < 5 && intersections.size() > 1; i++)
	{
	    int index = rand.nextInt(intersections.size() - 1) + 1;
	    RoadNode removed = intersections.remove(index);
	    removed.die();
	}
	Spawner root = new Spawner();
	root.entrypoint = node;
	root.spawnWeight = 0.05f;
	spawners.add(root);
    }

    private RoadNode containsPosition(int x, int y)
    {
	for (RoadNode node : intersections)
	{
	    if (x == node.x && y == node.y)
		return node;
	}
	return null;
    }

    private void populate(RoadNode current, int width, int height)
    {
	intersections.add(current);
	int x = current.x;
	int y = current.y;
	if (x >= width || y >= height)
	    return;
	RoadNode right = containsPosition(x + 10, y);
	if (right == null)
	{
	    RoadNode next = new RoadNode(x + 10, y);
	    current.addConnection(0, next, 10, 0.05f);
	    populate(next, width, height);
	} else
	{
	    current.addConnection(0, right, 10, 0.05f);
	}
	RoadNode down = containsPosition(x, y + 10);
	if (down == null)
	{
	    RoadNode next = new RoadNode(x, y + 10);
	    current.addConnection(3, next, 10, 0.05f);
	    populate(next, width, height);
	} else
	{
	    current.addConnection(3, down, 10, 0.05f);
	}
    }
}
