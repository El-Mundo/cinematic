var font_chn, font_eng;

function preload() {
	//font_chn = loadFont("./res/font/SourceHanSerifSC-Regular.otf");
	//font_eng = loadFont("./res/font/SourceHanSerifSC-Regular.otf");
}

function setup() {
	createCanvas(windowWidth, windowHeight);
	background(0);
}

function draw() {
	fill('#ED225D');
	//textFont(font_chn);
	textSize(36);
	text('p5*js', 10, 50);
}
