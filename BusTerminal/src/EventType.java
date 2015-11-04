
public enum EventType{
	PERSON_ARRIVAL{
		public String toString(){
			return "PERSON_ARRIVAL";
		}
	},
	BUS_ARRIVAL{
		@Override
		public String toString(){
			return "BUS_ARRIVAL";
		}
	},
	PERSON_BOARD{
		@Override
		public String toString(){
			return "PERSON_BOARD";
		}
	};
}
