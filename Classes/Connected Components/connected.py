import networkx as nx
from random import randint
import matplotlib.pyplot as plt
import seaborn as sb
import pandas as pd

NUM_SAMPLES = 100
MIN_NODES = 10
MAX_NODES = 200
STEP = 10

x_axis = range(MIN_NODES, MAX_NODES + 1, STEP)
results = dict()

for node_amount in range(MIN_NODES, MAX_NODES + 1, STEP):
    result_list = []

    for sample in range(NUM_SAMPLES):
        # Add nodes
        G = nx.Graph()

        for j in range(node_amount):
            G.add_node(j)
        
        # Add edges until it's connected
        edge_counter = 0
        
        while not nx.is_connected(G):
            node1 = randint(0, node_amount)
            node2 = randint(0, node_amount)

            if node1 == node2 or G.has_edge(node1, node2):
                continue
            
            G.add_edge(node1, node2)
        
        result_list.append(len(G.edges))
    # results.append(result_list)
    results[node_amount] = result_list


df = pd.DataFrame(results)
fig, ax = plt.subplots(2, figsize=(10, 15))

sb.boxplot(data=df, ax=ax[0])
sb.lineplot(data = df.mean(), ax=ax[1], markers=True)
plt.savefig('graph.pdf')