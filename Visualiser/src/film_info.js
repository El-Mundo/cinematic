var display_film_key = 'qiao';
var film;

let production, title;

function draw_film_page() {
	background(0);
	frame_offset_y = 0;
	textAlign(LEFT, TOP);

	//Draw film title
	textWrap((lan === 'chn') ? CHAR : WORD);
	textSize(32);
	fill(255);
	text(title, 12, 12);
	textSize(20);
	text(production, 12, 64);
}

function show_film_page(film_key) {
	display_film_key = film_key;
	film = filmMap.get(film_key);
	let useChn = (lan === 'chn');

	title = (useChn ? film.title : film.translated);
	let productionObjs = film.production;
	let str = '';
	for(let i = 0; i < productionObjs.length; i++) {
		str = str.concat((useChn ? productionObjs[i].chn : productionObjs[i].eng) + ' & ');
	}
	str = str.substring(0, str.length - 3);
	production = str;
}

function update_film_page() {
	if(display_film_key.length > 0) {
		show_film_page(display_film_key);
	}
}