const PRESS_SCALE = 0.9;

class Interactable {
	constructor(x, y, width, height, image = null, image_scale = 1, caption_chn = "", caption_eng = "", caption_size = 20) {
		this.x = x;
		this.y = y;
		if(image == null) {
			this.width = width;
			this.height = height;
		}else {
			this.image = image;
			this.width = this.image.width * image_scale;
			this.height = this.image.height * image_scale;
		}
		this.image = this.image;
		this.inactive = false;
		this.caption_chn = caption_chn;
		this.caption_eng = caption_eng;
		this.centreX = x + width / 2;
		this.centreY = y + height / 2;
		this.caption_size = caption_size;
	}

	isSelected() {
		if(cursorX > this.x && cursorX < this.x + this.width) {
			if(cursorY > this.y && cursorY < this.y + this.height) {
				return true;
			}
		}
		return false;
	}

	offscreen() {
		return this.x < -this.width-frame_offset_x || this.y < -this.height-frame_offset_y || this.x > DEFAULT_WIDTH-frame_offset_x || this.y > DEFAULT_HEIGHT-frame_offset_y;
	}
	
	update() {
		if(this.offscreen()) {
			return;
		}

		fill(255);
		if(this.inactive) {
			if(this.image != null) {
				tint(220, 120, 0);
				image(this.image, this.x, this.y, this.width, this.height);
				noTint();
			}else{
				fill(220, 120, 0);
				rect(this.x, this.y, this.width, this.height);
			}
		}else if(this.isSelected()) {
			push();
			let tempW = this.width;
			let tempH = this.height;
			let tintVal = 0;
			if(clicked) {
				tintVal = 160;
				tempW = (int) (this.width * PRESS_SCALE);
				tempH = (int) (this.height * PRESS_SCALE);
			}else {
				tintVal = 220;
				if(pClicked) {
					this.interact();
				}
			}
			if(this.image != null) {
				tint(tintVal);
				image(this.image, this.x + (this.width - tempW) / 2, this.y + (this.height - tempH) / 2, tempW, tempH);
				noTint();
			}else {
				fill(tintVal);
				rect(this.x, this.y, this.width, this.height);
			}
			pop();
		}else {
			if(this.image != null) {
				image(this.image, this.x, this.y, this.width, this.height);
			}else {
				rect(this.x, this.y, this.width, this.height);
			}
		}
		textAlign(CENTER, CENTER);
		textSize(this.caption_size);
		fill(0);
		text((lan === 'chn' ? this.caption_chn : this.caption_eng), this.centreX, this.centreY);
		textAlign(LEFT, TOP);
	}
	
	interact() { }
}

class FilmSelectButton extends Interactable {
	constructor(x, y, width, height, image = null, image_scale = 1, film_key = "", caption_chn = "", caption_eng = "", caption_size = 20) {
		super(x, y, width, height, image, image_scale, caption_chn, caption_eng, caption_size);
		this.film_key = film_key;
	}
	
	interact() {
		if(!this.inactive) {
			show_film_page(this.film_key);
			state = 5;
		}
	}
}