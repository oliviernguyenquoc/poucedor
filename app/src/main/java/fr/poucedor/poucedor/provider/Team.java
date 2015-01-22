package fr.poucedor.poucedor.provider;

/**
 * Java classes for use by Retrofit
 */
public class Team {
    public String id;
    public String name;
    public Student student1;
    public Student student2;
    public Positions positions;
    public University university;

    public class Student {
        public String name;
        public String email;
    }

    public class Positions {
        public int count;
        public Position last;
        public Position furthest;

        public class Position {
            public String _id;
            public Location location;
            public long timestamp;
            public double distance;
        }
    }

    public class University {
        public String id;
        public String name;
        public Location location;
    }


    public class Location {
        public double lat;
        public double lon;
    }
}
