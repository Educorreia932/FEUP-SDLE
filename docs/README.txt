Node: Participante na rede social
Host cache: Conheçe as nodes participantes
Tópico: Tópico de um post. Neste momento é o ID do user que criou esse post.
Amigo: Node a que tens "relação" com a pelo menos um tópico.
Relação: Relação unidirecional de uma node (1) com outra(2) a um certo tópico é igual ao número de vezes que recebeu posts de um tópico sobre o número de vezes que recebeu posts
"Receber" posts de um tópico neste contexto quer dizer que um QueryHit com esse tópico passou por ti a meio de se propagar (incluíndo quem recebeu no final; excluindo quem enviou)
Cache: Contém quem enviou um Query (para se poder enviar QueryHit pelo caminho oposto)

Default run "cycle":
Node entra (Peer.connect):
Envia RequestConnect(Neste momento a cache é centralizada)
A cache adiciona a Node 1 às nodes que conheçe
A cache envia um ConnectTo à node 1
Se a cache tiver neighbours, a node dá Ping às outras nodes
Node 1 dá addNeighbour aos que recebeu da cache
Node 1 dá ping para conheçer novas nodes

Node adiciona algo aos seus posts (Peer.addPost)

Node pesquisa (Peer.search):
Cria uma mensagem query com uma keyword (neste momento é o id do user que postou)
Envia essa mensagem para:
Se não tiver friend(s) ou a sua relação a esse tópico com todos for 0, propaga para todos os vizinhos

        Senão só envia para esses friend(s)
    Quem recebe um query vê se tem o conteúdo com essa keyword.
        Se tiver envia um QueryHit no caminho oposto em que foi enviado

        Senão adiciona à Cache quem o propagou por último
        Dá update a quem o propagou por último para si própria
        Propaga o Query (da mesma maneira que o enviaria)




Situação onde alguém envia um addNeighbour e a outra pessoa já tem o nº de neighbours máximos:
Node 1 wants to connect to 2:
Node 1 sends ADD_NEIGHBOUR to 2
Node 2 is full.
Node 2 picks a random neighbour (eg: Node 3)
Node 2 sends Node 3 FORCE_DROP
Node 2 drops Node 3 from it's neighbours
Node 3 removes Node 2 from it's neighbours (It does not answer)


Cada node pesquisa por posts de cada um dos seus followers de certo em certo tempo, definível na file das contantes.




Explicação de parte de como isto funciona. Ainda n explica digests ou outras coisas