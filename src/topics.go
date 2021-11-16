package main

import (
	"container/list"
)

type Topic struct {
	name     string
	messages *list.List
	subs     map[string]*list.Element
}

func NewTopic(name string) *Topic {
	t := new(Topic)
	t.name = name
	t.messages = list.New()
	t.subs = make(map[string]*list.Element)

	return t
}

func (t *Topic) AddMessage(m string) {
	message_elem := t.messages.PushBack(m)

	for id, elem := range t.subs {
		if elem == nil {
			t.subs[id] = message_elem
		}
	}
}

func (t *Topic) AddSubscriber(id string) {
	t.subs[id] = nil
}

func (t *Topic) GetMessage(sub_id string) string {
	m := t.subs[sub_id].Value.(string)
	t.subs[sub_id] = t.subs[sub_id].Next()

	return m
}

type TopicList struct {
	topics map[string]*Topic
}

func NewTopicList(name string) *TopicList {
	t := new(TopicList)
	t.topics = make(map[string]*Topic)

	return t
}

func (t_list *TopicList) PublishMessage(topic string, m string) {
	// Create topic if it doesn't exist
	if t, ok := t_list.topics[topic]; ok { // Topic exists
		// TODO: Check for waiting clients and send latest message?
		t.AddMessage(m)
	} else {
		t_list.topics[topic] = NewTopic(topic)
		t_list.topics[topic].AddMessage(m)
	}
}
