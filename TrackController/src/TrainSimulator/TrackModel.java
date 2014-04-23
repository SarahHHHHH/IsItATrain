/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author yanisoukaci
 */
package TrainSimulator;
import java.io.*;
import java.util.*;
public class TrackModel
{
    ArrayList<Block> blocks = new ArrayList<Block>();
    ArrayList<TrackController> lineControllers = new ArrayList<TrackController>();
    ArrayList<Switch> switches = new ArrayList<Switch>();
    HashMap<TrackController,int[]> controllers = new HashMap<>();
    ArrayList<TrackController> cont_list = new ArrayList<>();
    HashMap<String, Integer> stationList = new HashMap<>();
    HashMap<Integer, Integer> trains = new HashMap<>();
    String trackID;
    String block_line;
    PhantomCTCGUI ctc;
    public TrackModel(String filename, PhantomCTCGUI ctcIn)
    {
        try
        {
            BufferedReader initializer = new BufferedReader(new FileReader(filename));
            ctc = ctcIn;
            
            trackID = initializer.readLine();
            Block yard = new Block("0 1000 0 75 0 none no no yes 12");
            blocks.add(yard);
            while(initializer.ready())
            {
                block_line = initializer.readLine();
                Block b = new Block(block_line);
                blocks.add(b);
                
                if(b.stationOnBlock())
                {
                    stationList.put(b.setBeacon(), b.getBlockID());
                }
            }
            initializer.close();
            
            for(int i = 0; i < blocks.size(); i++)
            {
                Switch s;
                Block b = blocks.get(i); //get the current block
                if(b.switchOnBlock()) //does current block have a switch
                {
                    if(blocks.get(i - 1).switch_id != -1)
                    {
                        s = new Switch(b.switch_id, i, i + 1);
                    }
                    else
                    {
                        s = new Switch(b.switch_id, i, i - 1);
                    }
                    
                    //go through all the blocks again
                    for(int j = 0; j < blocks.size(); j++)
                    {
                        Block x = blocks.get(j); //get current block
                        
                        //dont get identical blocks and dont get block next to this block
                        if(x.switch_id == b.switch_id && j != i && j != i+1 && j != 0)
                        {
                            s.setOpenBlock(j);
                            break;
                        }
                    }
                    switches.add(s);
                    
                }
                
            }
            
            //Train is on the first block in the arraylist -> in yard -> block Id 0!!
            blocks.get(0).setTrain(true);
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        
        int cont_count = 0;
        int block_count = 0;
        
        for(int i = 3; i <= blocks.size(); i++)
        {
            if(blocks.size() % i == 0)
            {
                cont_count = i;//number of controllers
                block_count = blocks.size() / i; //number of blocks per controller
                break;
            }
        }
        for(int i = 0; i < cont_count; i++)
        {
            int start_block = (block_count * i);
            if(i > 0)
            {
                start_block++;
            }
            int end_block = (start_block + block_count);
            if(end_block > blocks.size())
            {
                end_block = blocks.size();
            }
            TrackController c = new TrackController(i + 1, start_block, end_block, this, ctc);
            int[] a = {start_block,end_block};
            controllers.put(c, a);
            this.lineControllers.add(c);
            cont_list.add(c);
        }
    }
    
    public ArrayList<TrackController> getLineControllers()
    {
        return (this.lineControllers);
    }
    
    public int getTrackSize()
    {
        return blocks.size();
    }
    public void trackSwitch(int id, int oc)
    {
        //1 for open, 0 for close
        for(Switch s : switches)
        {
            if(s.block_id[0] == id)
            {
                s.activateSwitch(oc);
            }
        }
    }
    //for train model
    public double getSpeedLimit(int block_num)
    {
        Block b = blocks.get(block_num);
        return b.speedLimit();
    }
    //for train model, from track controller
    public double getSpeed(int block_num, int train_id)
    {
        TrackController temp =cont_list.get(0);
        for (TrackController c : controllers.keySet())
        {
            int[] block_range = controllers.get(c);
            
            if(block_num >= block_range[0] && block_num <= block_range[1])
            {
                temp = c;
                break;
            }
        }
        return temp.getSpeed(train_id, block_num);
    }
    //for train model, from track controller
    public double getAuthority(int block_num, int train_id)
    {
        TrackController temp = cont_list.get(0);
        for (TrackController c : controllers.keySet())
        {
            int[] block_range = controllers.get(c);
            
            if(block_num >= block_range[0] && block_num <= block_range[1])
            {
                temp = c;
                break;
            }
        }
        return temp.getAuthority(train_id);
    }
    
    public ArrayList<Switch> getSwitchList()
    {
        return this.switches;
    }
    public int getStartingBlock()
    {
        return 0;
    }
    public String getBeacon(int block_num)
    {
        Block b = blocks.get(block_num);
        return b.setBeacon();
    }
    public double getGrade(int block_num)
    {
        return blocks.get(block_num).grade();
    }
    public double getBlockLength(int block_num)
    {
        return blocks.get(block_num).size();
    }
    public int getNextBlock(int curr, int prev, int trainID)
    {
        int next = curr + 1;
        if (next >= blocks.size())
        {
            next = 1;
        }
        if(blocks.get(curr).switchOnBlock())
        {
            for(Switch s : switches)
            {
                if(blocks.get(curr).switch_id == s.id)
                {
                    next = s.next_block;
                }
            }
        }
        
        blocks.get(curr).setTrain(false);
        blocks.get(next).setTrain(true);
        
        if (next >= blocks.size()-1){
            //don't check for crossing
        }
        //check if there is a crossing on the next block
        else{
            if(blocks.get(next+1).crossingOnBlock())
            {
                TrackController temp = null;
                for (TrackController c : controllers.keySet())
                {
                    int[] block_range = controllers.get(c);

                    if(next >= block_range[0] && next <= block_range[1])
                    {
                        temp = c;
                        break;
                    }
                }
                ctc.setCrossBarStatus("Dropped Crossbar", trackID);
                temp.dropCrossBar(next + 1, this);
            }
        }
        if(blocks.get(curr).crossingOnBlock())
        {
            TrackController temp = null;
            for (TrackController c : controllers.keySet())
            {
                int[] block_range = controllers.get(c);

                if(next >= block_range[0] && next <= block_range[1])
                {
                    temp = c;
                    break;
                }
            }
            ctc.setCrossBarStatus("Raised Crossbar", trackID);
            temp.raiseCrossBar(curr, this);
        }
        
        ctc.setCurrBlock(next, trainID);
        trains.remove(trainID);
        trains.put(trainID, next);
        
        return next;
    }
    public int getTrainBlockID()
    {
        int curr = 0;
        for(Block b : blocks)
        {
            if(b.trainOnBlock())
            {
                curr = b.getBlockID();
                break;
            }
        }
        
        return curr;
    }
    public int getTrainID(int b)
    {
        if(trains.containsValue(b))
        {
            for(int t : trains.keySet())
            {
                if(trains.get(t) == b)
                {
                    return t;
                }
            }
        }

        return 0;
    }
    public boolean getUnderground(int block_num)
    {
        return blocks.get(block_num).isUnderground();
    }
    public double getDt()
    {
        TrackController temp = cont_list.get(0);
        return temp.getDt();
    }
    public void addTrainToTrack(int t)
    {
        trains.put(t, 0);
    }
    
    public HashMap<String, Integer> getStations()
    {
        return stationList;
    }
    
    public String toString()
    {
        return trackID;
    }
}
