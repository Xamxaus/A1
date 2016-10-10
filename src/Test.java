import java.util.Arrays;
import java.util.Scanner;

public class Test {

	public static void main(String[] args) {
		//generate board
		int boradSize = 4;
		int robotInitializeX = 0;
		int robotInitializeY = 0;
		char robotInitializeDirection = 'w';
	
		Board boardTest = new Board(boradSize, robotInitializeX, robotInitializeY, robotInitializeDirection);
		State initial = new State(null,boardTest.getNumOfDirt(), boardTest.getDirts(), 0, boardTest.getRobotPosition(), boardTest.getRobotDirection());
		//random set dirt and obstacle, the parameter is the number of dirt or obstacles
		boardTest.randomSetDirt(3);
		boardTest.randomSetObstacle(5);
		//search
		//DFS
		System.out.println("DFS: ");
		State solution = DFS(boardTest);		
		System.out.println(solution);
		
		//BFS
		boardTest.reset(initial);
		System.out.println("BFS: ");
		solution = BFS(boardTest);
		System.out.println(solution);
		System.out.println("we are here 2");
		
		//A*
		boardTest.reset(initial);
		System.out.println("A*: ");
		solution = Astar(boardTest);
		System.out.println(solution);
		System.out.println("we are here 3");
		
//		// TODO Auto-generated method stub
//		DoublyLinkedList l= new DoublyLinkedList();
//		int[] pos = {1,1};
//		int[][] dirtPos = {{1,2},{2,3},{2,2}};
//		State s1 = new State(null, 3,dirtPos, 0, pos,'w');
//		pos[1] = 2;
//		Cell robot2 = new Cell(false,false,pos);
//		State s2 = new State(null, 2,dirtPos, 10, pos,'n');
//		pos[0] = 2;
//		Cell robot3 = new Cell(false,false,pos);
//		State s3 = new State(null, 0,dirtPos, 30, pos,'e');
//		
//		l.addFirst(s1);
//		l.addLast(s2);
//		l.addLast(s3);
//		System.out.println(s3);
//		System.out.println(l.onList(new State(null, 3,dirtPos, 0, pos,'s')));
		
	}
	
	//DFS with stack
	public static State DFS(Board board){
		Stack open = new Stack();
		Stack closed = new Stack();
		State initial = new State(null,board.getNumOfDirt(), board.getDirts(), 0, board.getRobotPosition(), board.getRobotDirection());
		System.out.println(initial);
		open.addLast(initial);
		while(!open.isEmpty()){
			State temp = open.pop();
			closed.addLast(temp);
			if(temp.getNumDirt()==0){
				return temp;
			}
			else{
				State child1 = board.children(temp,'w');
				State child2 = board.children(temp,'e');
				State child3 = board.children(temp,'n');
				State child4 = board.children(temp,'s');
				if(child1 != null && !open.onList(child1) && !closed.onList(child1)){
					open.push(child1);
				}
				if(child2 != null && !open.onList(child2) && !closed.onList(child2)){
					open.push(child2);
				}
				if(child3 != null && !open.onList(child3) && !closed.onList(child3)){
					open.push(child3);
				}
				if(child4 != null && !open.onList(child4) && !closed.onList(child4)){
					open.push(child4);
				}
			}
		}
		return null;
	}
	//BFS with queue
	public static State BFS(Board board){
		Queue open = new Queue();
		Queue closed = new Queue();
		State initial = new State(null,board.getNumOfDirt(), board.getDirts(), 0, board.getRobotPosition(), board.getRobotDirection());
		System.out.println(initial);
		open.enqueue(initial);;
		while(!open.isEmpty()){
			State temp = open.dequeue();
			closed.enqueue(temp);
			if(temp.getNumDirt()==0){
				return temp;
			}
			else{
				State child1 = board.children(temp,'w');
				State child2 = board.children(temp,'e');
				State child3 = board.children(temp,'n');
				State child4 = board.children(temp,'s');
				if(child1 != null && !open.onList(child1) && !closed.onList(child1)){
					open.enqueue(child1);
				}
				if(child2 != null && !open.onList(child2) && !closed.onList(child2)){
					open.enqueue(child2);
				}
				if(child3 != null && !open.onList(child3) && !closed.onList(child3)){
					open.enqueue(child3);
				}
				if(child4 != null && !open.onList(child4) && !closed.onList(child4)){
					open.enqueue(child4);
				}
			}
		}
		return null;
	}
	//A* with stack and heuristic function
	public static State Astar(Board board){
		Stack open = new Stack();
		Stack closed = new Stack();
		State initial = new State(null,board.getNumOfDirt(), board.getDirts(), 0, board.getRobotPosition(), board.getRobotDirection());
		System.out.println(initial);
		System.out.println("num of dirt is: " + board.getNumOfDirt());
		int[][] dirtPositions = board.getDirts();
		for(int i = 0; i < board.getDirts().length; i++){
			
			System.out.print("dirt at: ");
			System.out.println(dirtPositions[i][0] + " " + dirtPositions[i][1]);
		}
		
		open.addLast(initial);
		while(!open.isEmpty()){
			State temp = open.pop();
			closed.addLast(temp);
			if(temp.getNumDirt()==0){
				return temp;
			}
			else{
				State[] child = new State[4];
				child[0] = board.children(temp,'w');
				child[1] = board.children(temp,'e');
				child[2] = board.children(temp,'n');
				child[3] = board.children(temp,'s');
				//write heuristic function here to decide the order of children
				//f(x) = g(x) + h(x)
				//g(x) is the energy cost(already taken)
				//Assume that h(x) is the sum of distance of for the robot to connect each dirt point
				int[] h = {-1,-1,-1,-1};
				if(child[0] != null && !open.onList(child[0]) && !closed.onList(child[0])){
					h[0] = child[0].getEnergyCost() + h(child[0],board.getSize());
				}
				if(child[1] != null && !open.onList(child[1]) && !closed.onList(child[1])){
					h[1] = child[1].getEnergyCost() + h(child[1],board.getSize());
				}
				if(child[2] != null && !open.onList(child[2]) && !closed.onList(child[2])){
					h[2] = child[2].getEnergyCost() + h(child[2],board.getSize());
				}
				if(child[3] != null && !open.onList(child[3]) && !closed.onList(child[3])){
					h[3] = child[3].getEnergyCost() + h(child[3],board.getSize());
				}
				Arrays.sort(h);
				for(int i=3;i>-1;i--){
					if(h[i] != -1){
						open.push(child[i]);
					}
				}
			}
		}
		return null;
	}
	
	public static int h(State cur, int bound){
		System.out.println(cur);
		Scanner s = new Scanner(System.in);
		int a = s.nextInt();
		Board newb = new Board(bound, cur.getRobot()[0],cur.getRobot()[1], cur.getRobotDir());
		newb.randomSetObstacle(0);
//		System.out.println(cur.getNumDirt());
		for(int i=0;i<cur.getNumDirt();i++){
//			System.out.println(cur.getDirtPos()[i][0] + " " + cur.getDirtPos()[i][1]);
			newb.setDirt(cur.getDirtPos()[i][0], cur.getDirtPos()[i][1]);
		}
		return BFS(newb).getEnergyCost();
	}
}
