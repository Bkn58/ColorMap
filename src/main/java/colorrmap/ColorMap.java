import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Bkn
 *
 * задача о раскрашивании карты сводится, в сущности, к определению минимального числа красок.
 * (Уэзерелл "Этюды для программистов")
 */
public class ColorMap {
    // отдельная страна
    public class State {
        String namestat;                // Название страны
        int color;                      // цвет на карте
        ArrayList<String> neighbors;    //соседние страны

        public State () {
            namestat = "";
            color = 0;
            neighbors = new ArrayList<String>();
        }
    }
    ArrayList <State> atlas;            // список стран

    public ColorMap () {
        atlas = new ArrayList<State>();
    }

    /**
     * Чтение графа связей между странами
     * @param path - путь к текстовому файлу
     *  названия стран вводятся построчно. В строке первой идет название страны, далее через запятые названия стран, с которыми
     *  она граничит.
     *   Например:
     *  WA,ID,OR
     *  ID,WA,OR,NV,UT,WY,MT
     *  MT,ID,WY,ND,SD.....
     *  .........
     */
    public void ReadFromFile (String path) {
        String txtLine;

        System.out.println(path);
        File fIn = new File(path);
        try (BufferedReader inputVar = Files.newBufferedReader(fIn.toPath(), Charset.forName("Cp1251")))
        {txtLine = inputVar.readLine();
            while(txtLine != null){
                String [] aAttr = txtLine.split(","); // получаем массив стран

                InsertIntoCollection (aAttr);         // записываем страны в динамический массив
                txtLine = inputVar.readLine();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ColorMap.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ColorMap.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Записывает в коллекцию стран очередную страну
     * @param aState - массив стран (первая - название этой страны, остальные - соседи)
     */
    public void InsertIntoCollection (String [] aState) {
        if(aState.length==0) return;
        State state = new State();
        state.namestat = aState[0];
        state.color = 0;
        state.neighbors.addAll(Arrays.asList(aState));
        state.neighbors.remove(0); // удаление из соседей самой этой страны
        atlas.add(state);

    }

    /**
     * Возвращает цвет страны в соответствии с ее названием
     * @param state - страна
     * @return int - номер цвета страны
     */
    public int GetStateColor (String state){
        for (State st:atlas) {
            if(st.namestat.equals(state)) return st.color;
        }
        System.out.println("State " + state + " not found");
        return 0;
    }

    /**
     * Устанавливает цвет страны
     * @param state - название страны
     * @param clr - цвет
     */
    public void SetStateColor (String state, int clr){
        for (State st:atlas) {
            if(st.namestat.equals(state)) st.color=clr;
        }
    }

    /**
     * Сколько существует таких же стран в коллекции (должна быть ровно одна)
     * @param astate - название страны
     * @return cnt - количество найденных стран
     */
    public int WhatExistState (String astate) {
        int cnt = (int)atlas.stream().filter(state-> state.namestat.equals(astate)).count();
        if (cnt==0) System.out.println ("State "+astate+ " not exist");
        else if (cnt>1) System.out.println ("State "+astate+ " exist more than once");
        return cnt;
    }

    /**
     * Является ли страна сама себе соседкой (не должно быть таких)
     * @param astate - элемент коллекции State
     * @return boolean
     */
    public boolean IsStateSelfNeighbors (State astate) {
        boolean exist = astate.neighbors.stream().anyMatch(state->state.equals(astate.namestat));
        if (exist) System.out.println ("State "+astate.namestat+ " is self Neighbors");
        return exist;
    }

    /**
     * Существуют ли страны из списка соседей данной страны
     * @param astate - элемент коллекции стран State
     * @return boolean - true (существует)
     */
    public boolean IsExistNeighbors (State astate) {
        boolean exist = astate.neighbors.stream().allMatch(state->WhatExistState(state)==1);
        if (!exist) System.out.println ("Not found any NeighborsState "+astate.neighbors);
        return exist;
    }

    /**
     * Проверяет страны из списка соседей на предмет, есть ли в этих странах среди соседей данная страна
     * @param astate - элемент коллекции стран State
     * @return - true- во всех странах из списка соседей присутствует среди соседей данная страна
     */
    public boolean ExistStateInNeighbors (State astate) {
        boolean exist=false;
        for (String st : astate.neighbors) {
            for (State state:atlas) {
                if (state.namestat.equals(st)) {
                    for (String str : state.neighbors) {
                        if (str.equals(astate.namestat)) {
                            exist = true;
                            break;
                        }
                        else exist = false;
                    }
                    if (!exist) {
                        System.out.println ("Not found in NeighborsState of "+state.namestat + " state "+astate.namestat);
                        return false;
                    }
                }
            }
        }
        return exist;
    }

    /**
     * Проверка всей коллекции на корректность
     * @return - true - коллекция корретна
     */
    public boolean IsCollectionValid () {
        boolean valid = atlas.stream().allMatch(state ->
                !IsStateSelfNeighbors(state) &&
                        IsExistNeighbors(state) &&
                        ExistStateInNeighbors(state) &&
                        WhatExistState(state.namestat)==1);
        return valid;
    }

    /**
     * Найти страну с максимальным числом соседей
     * @return String - название страны.
     */
    public String GetStateWithMaxNeighbors () {
        int max=0;
        String nmState="";
        for (State st:atlas) {
            int tmp= (int)st.neighbors.stream().count();
            if (tmp>max){
                nmState = st.namestat;
                max = tmp;
            }
        }
        return nmState;
    }

    /**
     * Очистка цвета у всех стран.
     */
    public void ClrAllColor () {
        for (State st:atlas) {
            st.color=0;
        }
    }

    /**
     * Ищет среди коллекции стран максимальный номер цвета.
     * @return - максимальный цвет в коллекции стран.
     */
    public int GetMaxColor () {
        State state = atlas.stream().max(Comparator.comparingInt(st->st.color)).get();
        return state.color;
    }

    public  void PrintAllState () {
        //
        System.out.println("Max color = "+GetMaxColor());
        for (State st:atlas) {
            switch (st.color) {
                case 1 : System.out.print("("+st.namestat +" красный "+st.color+")");
                    break;
                case 2 : System.out.print("("+st.namestat +" синий "+st.color+")");
                    break;
                case 3 : System.out.print("("+st.namestat +" желтый "+st.color+")");
                    break;
                case 4 : System.out.print("("+st.namestat +" зеленый "+st.color+")");
                    break;
                case 5 : System.out.print("("+st.namestat +" коричневый "+st.color+")");
                    break;
                case 6 : System.out.print("("+st.namestat +" голубой "+st.color+")");
                    break;
                case 7 : System.out.print("("+st.namestat +" фиолетовый "+st.color+")");
                    break;
            }
        }
        System.out.println ();
    }

    /**
     * Выполняет раскрашивание карты
     */
    public void Colorize () {
        Colors clrs = new Colors();
        for (State st: atlas) {
            clrs.FreeAllColors();
            if (st.color==0) {
                // у страны нет цвета, проверяем цвета соседей
                for (String nb:st.neighbors) {
                    clrs.SetBusy(GetStateColor(nb));
                }
                st.color = clrs.GetFreeColor();
            }
        }
    }

    public static void main (String[] args) {
        ColorMap clrMap = new ColorMap();
        clrMap.ReadFromFile("USA.csv");
        //clrMap.ReadFromFile("test.csv"); // любые два региона - соседи (каждый граничит с каждым)
        //clrMap.ReadFromFile("test2.csv");// никакие два региона не являются соседями
        if (clrMap.IsCollectionValid()) System.out.println("Collection of State is valid!");

        // Перебор всех вершин графа в качестве начальной вершины для раскраски
        for (State st: clrMap.atlas) {
            clrMap.ClrAllColor();
            st.color = 1;
            clrMap.Colorize();
            if (clrMap.GetMaxColor()<=4) {
                System.out.println("\nНачало с "+st.namestat);
                clrMap.PrintAllState();
            }
        }
    }
}
