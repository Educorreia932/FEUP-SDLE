package activitypub

class Activity: Object() {
    lateinit var actor: Actor
    lateinit var objects: Object
    lateinit var target: Activity
    lateinit var result: Object
}