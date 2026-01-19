# 🍕 LVT Pizza — Pasūtījumu Pārvaldības Sistēma

Vienkārša un ērta programma picērijas pasūtījumu pieņemšanai un ēdienkartes pārvaldībai. Izstrādāta ar Java Swing.

---

## ✨ Galvenās Iespējas

* **🛒 Pasūtīšana**: Pievieno picas, uzkodas un dzērienus ar dažiem klikšķiem.
* **🍕 Pielāgojami Izmēri**: Automātiska cenu pārrēķināšana picām (30cm, 40cm, 50cm) un dzērieniem.
* **🛠️ Administrācijas Panelis**: Pievieno vai rediģē produktus un to cenas tieši no programmas saskarnes.
* **📊 Datu Drošība**: Automātiska pasūtījumu vēstures un produktu saraksta saglabāšana lokālos `.txt` failos.

---

## 🛠️ Tehnoloģijas

* **Valoda**: Java 21 (OpenJDK)
* **Grafika**: Java Swing & AWT

---

## 📥 Uzstādīšana un Palaišana

> **Svarīgi**: Pārliecinieties, ka jūsu datorā ir instalēta Java 21 vai jaunāka versija.

1.  **Lejupielāde**: Dodieties uz [Releases](../../releases) un lejupielādējiet `LVTpizza.jar`.
2.  **Mape**: Ievietojiet failu atsevišķā mapē.
3.  **Palaišana**:
    * Veiciet dubultklikšķi uz faila vai izmantojiet komandrindu (CMD):
    ```cmd
    java -jar LVTpizza.jar
    ```

---

## 📂 Datu Struktūra

Programma automātiski uztur divus failus tajā pašā mapē, kur atrodas `.jar` fails:

| Fails | Apraksts |
| :--- | :--- |
| `products.txt` | Satur visu picu un dzērienu sarakstu ar cenām. |
| `orders.txt` | Glabā visu veikto pasūtījumu vēsturi. |

---

## 👨‍💻 Autors
Izstrādāts kā mācību projekts, demonstrējot Java Swing un datu apstrādes prasmes.
