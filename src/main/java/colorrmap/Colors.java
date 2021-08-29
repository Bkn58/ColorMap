/**
 * Класс для работы с цветом
 * @author Bkn
 */
public class Colors {
    public class Color {
        int tone;
        boolean isBusy;
    }
    Color[] allColors = new Color[7]; // резервируем 7 цветов

    /**
     *  конструктор
     */
    public Colors () {
        for (int i=0;i<allColors.length;i++) {
            allColors[i] = new Color();
            allColors[i].tone = i+1;
            allColors[i].isBusy = false;
        }
    }
    /**
     * Очистка флага "занятый"
     */
    public void FreeAllColors () {
        for (Color color : allColors) {
            color.isBusy = false;
        }
    }

    /**
     * Установка флага в состояние "занятый"
     * @param tone - цвет, которому устанавливается флаг "занято"
     */
    public void SetBusy (int tone){
        for (Color color : allColors) {
            if (color.tone==tone) color.isBusy = true;
        }
    }

    /**
     * Ищет свободный цвет, после нахождения устанавливает его в состояние "занято"
     * @return - номер свободного цвета. 0 - свободных цветов не осталось.
     */
    public int GetFreeColor () {
        for (Color color : allColors) {
            if (!color.isBusy) {
                color.isBusy = true;
                return color.tone;
            }
        }
        return 0;
    }

}
