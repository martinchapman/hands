var step, play;

function showGraph() {
    
	var width = $(document).width();
	
    var height = $(document).height() - 100;

    var g = new Graph();
	
	var graph = graphNodes.split(",");
	
	var hiddenItems = hidden[$('#round-to-play').val()].substring(0, hidden[$('#round-to-play').val()].length - 1).split(",");
	
	for (var k = 0; k < hiddenItems.length; k++) {
		
		hiddenItems[k] = hiddenItems[k].substring(1, hiddenItems[k].length);
		
		g.addNode(hiddenItems[k], { label : hiddenItems[k] + " HIDE" });
	
		$('#hidden').append(hiddenItems[k] + " ");
	
	}
	
	for (var i = 0; i < graph.length; i++) {
		
		g.addEdge(graph[i].substring(graph[i].indexOf("(") + 1, graph[i].indexOf(":") - 1), graph[i].substring(graph[i].indexOf(":") + 2, graph[i].indexOf(")")));
		 
	}
	
	var layouter = new Graph.Layout.Spring(g);
	
	var searchPath = path[$('#round-to-play').val()].substring(0, path[$('#round-to-play').val()].length - 1).split(",");
	
	var renderer = new Graph.Renderer.Raphael('canvas', g, width, height);
	
	step = function() {
		
		drawPath(hiddenItems, searchPath, g, renderer);
		
		searchPath.splice(0,1);
		
	}
	
	play = function() {
		
		setInterval(function(){step()},1000);
		
	}
		
	step();
	
};

function drawPath(hiddenItems, searchPath, g, renderer) {
	
	
	for (var j = 1; j < searchPath.length; j++) {
		
	
		 /* colourising the shortest paths and setting labels */
		for(e in g.edges) {
			
			if((searchPath[j-1].substring(1,searchPath[j-1].length) == g.edges[e].source.id && searchPath[j].substring(1,searchPath[j].length) == g.edges[e].target.id) ||
			   (searchPath[j-1].substring(1,searchPath[j-1].length) == g.edges[e].target.id && searchPath[j].substring(1,searchPath[j].length) == g.edges[e].source.id)) {
			
				$('#output').html(searchPath[j-1].substring(1,searchPath[j-1].length) + " to " + searchPath[j].substring(1,searchPath[j].length));
				
				for (var l = 0; l < hiddenItems.length; l++) {
				
					if ( hiddenItems[l] == g.edges[e].source.id || hiddenItems[l] == g.edges[e].target.id ) {
						
						//alert(hiddenItems[l] + " " + g.edges[e].source.id);
						//alert(hiddenItems[l] + " " + g.edges[e].target.id);
						hiddenItems.splice(l, 1);
						$('#hidden').html("Hidden Items: ");
						
						for (var o = 0; o < hiddenItems.length; o++) {
		
							//alert(hiddenItems);
							
							$('#hidden').append(hiddenItems[o] + " ");
						
						}
	
					}
					
				}
				
				g.edges[e].style.stroke = "#00c8a1";
				g.edges[e].style.fill = "#00c8a1";
				
				renderer.draw();
				return;
				
			} else {
				
				g.edges[e].style.stroke = "#fff";
			
			}
			
		}
		
	}

}

function getRandomColor() {
	
    var letters = '0123456789ABCDEF'.split('');
    var color = '#';
    for (var i = 0; i < 6; i++ ) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
	
}