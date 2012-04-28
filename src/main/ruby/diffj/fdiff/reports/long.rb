#!/usr/bin/jruby -w
# -*- ruby -*-

require 'rubygems'
require 'riel'
require 'java'
require 'diffj/fdiff/reports/report'
require 'diffj/fdiff/writers/ctx_hl'
require 'diffj/fdiff/writers/ctx_no_hl'
require 'diffj/fdiff/writers/no_context'

include Java

include DiffJ::FDiff::Writer

# Reports differences in long form.
module DiffJ::FDiff::Report
  class LongReport < BaseReport
    include Loggable
    
    def initialize writer, show_context, highlight
      super writer

      @dwcls = show_context ? (highlight ?  ContextHighlightWriter : ContextNoHighlightWriter) : NoContextWriter
      @from_contents = nil
      @to_contents = nil
    end

    def write_differences
      from_lines = @from_contents.split "\n"
      to_lines = @to_contents.split "\n"
      dw = @dwcls.new from_lines, to_lines
      
      differences.each do |fdiff|
        str = dw.difference fdiff
        @writer.write str
      end
      
      @writer.flush
      # we can't close STDOUT:
      # writer.close
    end

    def reset from_file_name, from_contents, to_file_name, to_contents
      @from_contents = from_contents
      @to_contents = to_contents
      super
    end
  end
end