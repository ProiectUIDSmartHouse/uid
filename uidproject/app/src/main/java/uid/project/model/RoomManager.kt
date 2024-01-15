package uid.project.model

object RoomManager {
    val roomList: MutableList<Room> = mutableListOf()

    fun addRoom(roomName: String) {
        roomList.add(Room(roomName,false))
    }
}