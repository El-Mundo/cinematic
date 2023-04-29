//Default display size (will be adjusted according to window size)
const DEFAULT_WIDTH = 1080, DEFAULT_HEIGHT = 720;
//The ratio of the frame size to the window size
var frame_margin_ratio = 0.9;
//The ratio for scaling the frame size to fit window size
var frame_scale = 1.0;
//For translating the frame
var frame_offset_x = 0, frame_offset_y = 0;

//Mouse states
var cursorX, cursorY, pCursorX, pcursorY;
var clicked = false, pClicked = false;

//A out-of-frame slider to adjust the margin of the frame
let margin_slider;

//'chn' or 'eng'
var lan = 'eng';

//-1-loading data, 0-start menu, 1-catalogue, 2-map, 3-visualiser, 4-studio page, 5-film page
var state = -1, pState = -1;

function preload() {
	loadFonts();
	loadData();
}

function setup() {
	//Basic settings
	frameRate(30);
	smooth();
	noCursor();

	//Adapt display rate to the screen
	frame_scale = parseFloat(windowHeight) / parseFloat(DEFAULT_HEIGHT);
	let wantedWidth = (int)(DEFAULT_WIDTH * frame_scale);
	createCanvas(wantedWidth * frame_margin_ratio, windowHeight * frame_margin_ratio);

	//Init margin slider (html widget)
	margin_slider = createSlider(0.75, 0.95, frame_margin_ratio, 0.025);
	margin_slider.position(12, height);
	margin_slider.style('width', '80px');
	margin_slider.input(reset_margin_ratio);

	background(0);
}

function draw() {
	push();
	scale(frame_scale * frame_margin_ratio);
	translate(frame_offset_x, frame_offset_y);
	textFont((lan === 'chn') ? font_chn : 'Arial');
	switch (state) {
		case -1:
			draw_loading_scene();
			if(loaded_resource >= RESOURCE_NUM) {
				state = 1;
				show_all_films_in_catalogue();
			}
			break;
		case 1:
			draw_catalogue();
			break;
		case 5:
			draw_film_page();
			break;
		default:
			draw_start_menu();
			break;
	}
	pop();

	pCursorX = cursorX;
	pcursorY = cursorY;
	pClicked = clicked;
	pState = state;
	cursorX = (int)(parseFloat(mouseX) / frame_scale / frame_margin_ratio - frame_offset_x);
	cursorY = (int)(parseFloat(mouseY) / frame_scale / frame_margin_ratio - frame_offset_y);

	if(state == 5) {
		fill(255);
		rect(width-12, -64, 12, height+64);
		fill(128);
		rect(width-10, map(catalogue_scroll, 0, max_catalogue_scroll, 0, height-CATALOGUE_SLIDER_SIZE), 8, CATALOGUE_SLIDER_SIZE);
	}

	fill(255);
	circle(mouseX, mouseY, 6.0 * frame_scale);
}

function switch_language() {
	if(lan == 'chn') {
		lan = 'eng';
	} else if(lan == 'eng') {
		lan = 'chn';
	}
	update_film_page();
}

function keyPressed() {
	if(keyCode == 80) {
		switch_language();
	}
	if(keyCode == 38) {
		catalogue_scroll += 50;
	}else if(keyCode == 40) {
		catalogue_scroll -= 100;
	}
}

function mousePressed() {
	clicked = true;
}

function mouseReleased() {
	clicked = false;
}

function reset_margin_ratio() {
	frame_margin_ratio = margin_slider.value();
	windowResized();
}

function windowResized() {
	frame_scale = parseFloat(windowHeight) / parseFloat(DEFAULT_HEIGHT);
	let wantedWidth = (int)(DEFAULT_WIDTH * frame_scale);
	resizeCanvas(wantedWidth * frame_margin_ratio, windowHeight * frame_margin_ratio);

	margin_slider.position(12, height);
}
